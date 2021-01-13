package com.enricog.routines.detail.segment

import androidx.hilt.lifecycle.ViewModelInject
import com.enricog.base_android.viewmodel.BaseViewModel
import com.enricog.base_android.viewmodel.ViewModelConfiguration
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.entities.routines.TimeType
import com.enricog.routines.detail.segment.models.SegmentState
import com.enricog.routines.detail.segment.models.SegmentViewState
import com.enricog.routines.detail.segment.usecase.SegmentUseCase
import com.enricog.routines.navigation.RoutinesNavigationActions

internal class SegmentViewModel @ViewModelInject constructor(
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

    fun onSegmentNameTextChange(text: String) = runWhen<SegmentState.Data> { stateData ->
        state = reducer.updateSegmentName(stateData, text)
    }

    fun onSegmentTimeChange(seconds: Long) = runWhen<SegmentState.Data> { stateData ->
        state = reducer.updateSegmentTime(stateData, seconds)
    }

    fun onSegmentTypeChange(timeType: TimeType) = runWhen<SegmentState.Data> { stateData ->
        state = reducer.updateSegmentTimeType(stateData, timeType)
    }

    fun onSegmentConfirmed() = runWhen<SegmentState.Data> { stateData ->
        val errors = validator.validate(stateData.segment)
        state = if (errors.isEmpty()) {
            reducer.updateRoutineSegment(stateData)
        } else {
            reducer.applySegmentErrors(stateData, errors)
        }
    }

    fun onSegmentBack() = runWhen<SegmentState.Data> { stateData ->
        navigationActions.goBackToRoutines()
    }

}