package com.enricog.features.routines.detail.segment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.base.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutines.async.awaitAll
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.entities.seconds
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType.STOPWATCH
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentState.Data.Action.SaveSegmentError
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.usecase.GetTimerThemeUserCase
import com.enricog.features.routines.detail.segment.usecase.SegmentUseCase
import com.enricog.features.routines.detail.ui.time_type.TimeTypeStyle
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.SegmentRoute
import com.enricog.navigation.api.routes.SegmentRouteInput
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarEvent.ActionPerformed
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.textField.timeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
internal class SegmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    converter: SegmentStateConverter,
    private val reducer: SegmentReducer,
    private val getTimerThemeUserCase: GetTimerThemeUserCase,
    private val segmentUseCase: SegmentUseCase,
    private val validator: SegmentValidator,
    private val navigationActions: RoutinesNavigationActions
) : BaseViewModel<SegmentState, SegmentViewState>(
    initialState = SegmentState.Idle,
    dispatchers = dispatchers,
    converter = converter,
    configuration = ViewModelConfiguration(debounce = 0)
) {

    private val input = SegmentRoute.extractInput(savedStateHandle)
    private var saveJob by autoCancelableJob()
    private var loadJob by autoCancelableJob()

    var fieldInputs by mutableStateOf(SegmentInputs.empty)
        private set

    init {
        load(input = input)
    }

    private fun load(input: SegmentRouteInput) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable, message = "Error loading segment")
            updateState { reducer.error(throwable = throwable) }
        }

        loadJob = launch(exceptionHandler = exceptionHandler) {
            val (timerTheme, routine) = awaitAll(
                async { getTimerThemeUserCase() },
                async { segmentUseCase.get(routineId = input.routineId) }
            )
            updateState { reducer.setup(routine = routine, timerTheme = timerTheme, segmentId = input.segmentId) }

            runWhen<SegmentState.Data> { stateData ->
                val segment = stateData.segment
                fieldInputs = SegmentInputs(
                    name = segment.name.toTextFieldValue(),
                    time = segment.time.timeText
                )
            }
        }
    }

    fun onSegmentNameTextChange(textFieldValue: TextFieldValue) {
        updateStateWhen<SegmentState.Data> { stateData ->
            fieldInputs = fieldInputs.copy(name = textFieldValue)
            reducer.updateSegmentName(state = stateData)
        }
    }

    fun onSegmentTimeChange(text: TimeText) {
        updateStateWhen<SegmentState.Data> { stateData ->
            reducer.updateSegmentTime(state = stateData)
        }

        runWhen<SegmentState.Data> { stateData ->
            val time = when {
                stateData.selectedTimeType == STOPWATCH -> "".timeText
                text.toSeconds() > 3600.seconds -> fieldInputs.time
                else -> text
            }
            fieldInputs = fieldInputs.copy(time = time)
        }
    }

    fun onSegmentTypeChange(timeTypeStyle: TimeTypeStyle) {
        updateStateWhen<SegmentState.Data> { stateData ->
            reducer.updateSegmentTimeType(state = stateData, timeType = timeTypeStyle.toEntity())
        }

        runWhen<SegmentState.Data> { stateData ->
            if (stateData.selectedTimeType == STOPWATCH) {
                fieldInputs = fieldInputs.copy(time = "".timeText)
            }
        }
    }

    fun onSegmentSave() {
        runWhen<SegmentState.Data> { stateData ->
            val errors = validator.validate(
                inputs = fieldInputs,
                selectedTimeType = stateData.selectedTimeType
            )
            if (errors.isEmpty()) {
                save(
                    routine = stateData.routine,
                    segment = fieldInputs.mergeToSegment(
                        segment = stateData.segment,
                        type = stateData.selectedTimeType
                    )
                )
            } else {
                updateStateWhen<SegmentState.Data> {
                    reducer.applySegmentErrors(state = it, errors = errors)
                }
            }
        }
    }

    private fun save(routine: Routine, segment: Segment) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable, message = "Error saving segment")
            updateStateWhen<SegmentState.Data> {
                reducer.saveSegmentError(state = it)
            }
        }
        saveJob = launch(exceptionHandler = exceptionHandler) {
            segmentUseCase.save(routine = routine, segment = segment)
            navigationActions.goBack()
        }
    }

    fun onSegmentBack() {
        launch { navigationActions.goBack() }
    }

    fun onRetryLoad() {
        load(input = input)
    }

    fun onSnackbarEvent(snackbarEvent: TempoSnackbarEvent) {
        launchWhen<SegmentState.Data> {
            val previousAction = it.action
            updateStateWhen(reducer::actionHandled)
            delay(SNACKBAR_ACTION_DELAY)
            if (snackbarEvent == ActionPerformed) {
                when (previousAction) {
                    SaveSegmentError -> onSegmentSave()
                    null -> Unit
                }
            }
        }
    }

    private companion object {
        const val SNACKBAR_ACTION_DELAY = 100L
    }
}
