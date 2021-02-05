package com.enricog.routines.detail.segment

import androidx.lifecycle.viewModelScope
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.base_android.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutine.job.autoCancelableJob
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.segment.models.SegmentState
import com.enricog.routines.detail.segment.models.SegmentViewState
import com.enricog.routines.detail.segment.usecase.SegmentUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
internal class SegmentViewModel @Inject constructor(
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

    fun load(routineId: Long, segmentId: Long) {
        viewModelScope.launch {
            val routine = segmentUseCase.get(routineId = routineId)
            state = reducer.setup(routine = routine, segmentId = segmentId)
        }
    }

    fun onSegmentNameTextChange(text: String) = runWhen<SegmentState.Data> { stateData ->
        state = reducer.updateSegmentName(state = stateData, text = text)
    }

    fun onSegmentTimeChange(seconds: Long) = runWhen<SegmentState.Data> { stateData ->
        state = reducer.updateSegmentTime(state = stateData, seconds = seconds)
    }

    fun onSegmentTypeChange(timeType: TimeType) = runWhen<SegmentState.Data> { stateData ->
        state = reducer.updateSegmentTimeType(state = stateData, timeType = timeType)
    }

    fun onSegmentConfirmed() = runWhen<SegmentState.Data> { stateData ->
        val errors = validator.validate(segment = stateData.segment)
        if (errors.isEmpty()) {
            save(routine = stateData.routine, segment = stateData.segment)
        } else {
            state = reducer.applySegmentErrors(state = stateData, errors = errors)
        }
    }

    private fun save(routine: Routine, segment: Segment) {
        saveJob = viewModelScope.launch {
            segmentUseCase.save(routine = routine, segment = segment)
            navigationActions.routinesBack()
        }
    }

    fun onSegmentBack() = runWhen<SegmentState.Data> {
        navigationActions.routinesBack()
    }
}
