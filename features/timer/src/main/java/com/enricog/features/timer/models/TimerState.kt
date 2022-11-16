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

        val nextSegment: Segment?
            get() {
                val indexRunningSegment = routine.segments.indexOf(runningSegment)
                return routine.segments.getOrNull(indexRunningSegment + 1)
            }

        val nextSegmentStep: SegmentStep?
            get() = nextSegment?.let { getSegmentStepFrom(routine = routine, segment = it) }

        companion object {
            fun getSegmentStepFrom(routine: Routine, segment: Segment): SegmentStep {
                val (type, time) = if (segment.type != TimeType.REST && routine.startTimeOffset > 0.seconds) {
                    SegmentStepType.STARTING to routine.startTimeOffset
                } else {
                    SegmentStepType.IN_PROGRESS to segment.time
                }
                return SegmentStep(
                    count = Count.idle(seconds = time),
                    type = type
                )
            }
        }
    }

    data class Error(val throwable: Throwable) : TimerState()
}
