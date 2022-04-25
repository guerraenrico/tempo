package com.enricog.features.routines.detail.segment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.base.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.entities.Seconds
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.navigation.api.routes.SegmentRoute
import com.enricog.navigation.api.routes.SegmentRouteInput
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.usecase.SegmentUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.ui.components.textField.TimeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private var saveJob by autoCancelableJob()

    init {
        val input = SegmentRoute.extractInput(savedStateHandle)
        load(input)
    }

    private fun load(input: SegmentRouteInput) {
        viewModelScope.launch {
            val routine = segmentUseCase.get(routineId = input.routineId)
            updateState { reducer.setup(routine = routine, segmentId = input.segmentId) }
        }
    }

    fun onSegmentNameTextChange(text: String) {
        updateStateWhen<SegmentState.Data> { stateData ->
            reducer.updateSegmentName(state = stateData, text = text)
        }
    }

    fun onSegmentTimeChange(text: TimeText) {
        updateStateWhen<SegmentState.Data> { stateData ->
            reducer.updateSegmentTime(state = stateData, seconds = seconds)
        }
    }

    fun onSegmentTypeChange(timeType: TimeType) {
        updateStateWhen<SegmentState.Data> { stateData ->
            reducer.updateSegmentTimeType(state = stateData, timeType = timeType)
        }
    }

    fun onSegmentConfirmed() {
        runWhen<SegmentState.Data> { stateData ->
            val errors = validator.validate(segment = stateData.segment)
            if (errors.isEmpty()) {
                save(routine = stateData.routine, segment = stateData.segment)
            } else {
                updateStateWhen<SegmentState.Data> {
                    reducer.applySegmentErrors(state = it, errors = errors)
                }
            }
        }
    }

    private fun save(routine: Routine, segment: Segment) {
        saveJob = viewModelScope.launch {
            segmentUseCase.save(routine = routine, segment = segment)
            navigationActions.goBack()
        }
    }

    fun onSegmentBack() = launchWhen<SegmentState.Data> {
        navigationActions.goBack()
    }
}
