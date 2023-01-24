package com.enricog.features.timer.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.seconds

internal sealed class TimerState {

    object Idle : TimerState()

    data class Counting(
        val routine: Routine,
        val runningSegment: Segment,
        val step: SegmentStep,
        val isSoundEnabled: Boolean
    ) : TimerState() {

        val isStepCountRunning: Boolean
            get() = step.count.isRunning && !step.count.isCompleted

        val isStepCountCompleted: Boolean
            get() = step.count.isCompleted

        val isStepCountCompleting: Boolean
            get() = isStepCountRunning && !isStopwatchRunning &&
                    step.count.seconds <= STEP_COMPLETING_THRESHOLD

        val isRoutineCompleted: Boolean
            get() = routine.segments.indexOf(runningSegment) == routine.segments.size - 1 &&
                    step.type == SegmentStepType.IN_PROGRESS &&
                    step.count.isCompleted

        val isStopwatchRunning: Boolean
            get() = runningSegment.type == TimeType.STOPWATCH &&
                    step.type == SegmentStepType.IN_PROGRESS &&
                    isStepCountRunning

        val nextSegment: Segment?
            get() {
                val indexRunningSegment = routine.segments.indexOf(runningSegment)
                return routine.segments.getOrNull(indexRunningSegment + 1)
            }

        val nextSegmentStep: SegmentStep?
            get() = nextSegment?.let { SegmentStep.from(routine = routine, segment = it) }

        val previousSegment: Segment?
            get() {
                val indexRunningSegment = routine.segments.indexOf(runningSegment)
                return routine.segments.getOrNull(indexRunningSegment - 1)
            }

        val previousSegmentStep: SegmentStep?
            get() = previousSegment?.let { SegmentStep.from(routine = routine, segment = it) }

        private companion object {
            val STEP_COMPLETING_THRESHOLD = 5.seconds
        }
    }

    data class Error(val throwable: Throwable) : TimerState()
}
