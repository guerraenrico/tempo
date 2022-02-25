package com.enricog.features.timer.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType

internal sealed class TimerState {

    object Idle : TimerState()

    data class Counting(
        val routine: Routine,
        val runningSegment: Segment,
        val step: SegmentStep
    ) : TimerState() {

        val isCountRunning: Boolean
            get() = step.count.isRunning && !step.count.isCompleted

        val isCountCompleted: Boolean
            get() = step.count.isCompleted

        val isRoutineCompleted: Boolean
            get() = routine.segments.indexOf(runningSegment) == routine.segments.size - 1 &&
                step.type == SegmentStepType.IN_PROGRESS &&
                step.count.isCompleted

        val isStopwatchRunning: Boolean
            get() = runningSegment.type == TimeType.STOPWATCH &&
                step.type == SegmentStepType.IN_PROGRESS &&
                isCountRunning
    }
}
