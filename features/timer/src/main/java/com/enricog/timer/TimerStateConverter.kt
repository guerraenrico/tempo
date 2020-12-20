package com.enricog.timer

import androidx.compose.ui.graphics.Color
import com.enricog.base_android.viewmodel.StateConverter
import com.enricog.entities.routines.TimeType
import com.enricog.timer.models.SegmentStepType
import com.enricog.timer.models.TimerState
import com.enricog.timer.models.TimerViewState
import com.enricog.ui_components.resources.blue500
import com.enricog.ui_components.resources.darkBlue500
import com.enricog.ui_components.resources.orange500
import com.enricog.ui_components.resources.purple500
import javax.inject.Inject

internal class TimerStateConverter @Inject constructor() :
    StateConverter<TimerState, TimerViewState> {

    override suspend fun convert(state: TimerState): TimerViewState {
        return when (state) {
            TimerState.Idle -> TimerViewState.Idle
            is TimerState.Counting -> mapCounting(state)
        }
    }

    private fun mapCounting(state: TimerState.Counting): TimerViewState.Counting {
        return TimerViewState.Counting(
            runningSegment = state.runningSegment,
            step = state.step,
            clockBackgroundColor = state.getClockBackgroundColor()
        )
    }

    private fun TimerState.Counting.getClockBackgroundColor(): Color {
        return when {
            step.type == SegmentStepType.STARTING -> orange500
            runningSegment.type == TimeType.REST -> purple500
            runningSegment.type == TimeType.TIMER -> blue500
            runningSegment.type == TimeType.STOPWATCH -> darkBlue500
            else -> darkBlue500
        }
    }
}