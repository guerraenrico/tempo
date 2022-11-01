package com.enricog.features.routines.detail.segment

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.base.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentState.Data.Action.SaveSegmentError
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.usecase.SegmentUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.SegmentRoute
import com.enricog.navigation.api.routes.SegmentRouteInput
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarEvent.ActionPerformed
import com.enricog.ui.components.textField.TimeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
internal class SegmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatchers: CoroutineDispatchers,
    converter: SegmentStateConverter,
    private val reducer: SegmentReducer,
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

    init {
        load(input = input)
    }

    private fun load(input: SegmentRouteInput) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            TempoLogger.e(throwable = throwable, message = "Error loading segment")
            updateState { reducer.error(throwable = throwable) }
        }
        loadJob = launch(exceptionHandler = exceptionHandler) {
            val routine = segmentUseCase.get(routineId = input.routineId)
            updateState { reducer.setup(routine = routine, segmentId = input.segmentId) }
        }
    }

    fun onSegmentNameTextChange(textFieldValue: TextFieldValue) {
        updateStateWhen<SegmentState.Data> { stateData ->
            reducer.updateSegmentName(state = stateData, textFieldValue = textFieldValue)
        }
    }

    fun onSegmentTimeChange(text: TimeText) {
        updateStateWhen<SegmentState.Data> { stateData ->
            reducer.updateSegmentTime(state = stateData, text = text)
        }
    }

    fun onSegmentTypeChange(timeType: TimeType) {
        updateStateWhen<SegmentState.Data> { stateData ->
            reducer.updateSegmentTimeType(state = stateData, timeType = timeType)
        }
    }

    fun onSegmentConfirmed() {
        runWhen<SegmentState.Data> { stateData ->
            val errors = validator.validate(inputs = stateData.inputs)
            if (errors.isEmpty()) {
                save(
                    routine = stateData.routine,
                    segment = stateData.inputs.mergeToSegment(stateData.segment)
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

    fun onRetryLoadClick() {
        load(input = input)
    }

    fun onSnackbarEvent(snackbarEvent: TempoSnackbarEvent) {
        launchWhen<SegmentState.Data> {
            val previousAction = it.action
            updateStateWhen(reducer::actionHandled)
            delay(SNACKBAR_ACTION_DELAY)
            if (snackbarEvent == ActionPerformed) {
                when (previousAction) {
                    SaveSegmentError -> onSegmentConfirmed()
                    null -> Unit
                }
            }
        }
    }

    private companion object {
        const val SNACKBAR_ACTION_DELAY = 100L
    }
}
