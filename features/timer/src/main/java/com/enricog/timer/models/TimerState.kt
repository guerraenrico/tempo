package com.enricog.timer.models

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment

internal sealed class TimerState {

    object Idle : TimerState()

    data class Counting(
        val routine: Routine,
        val runningSegment: Segment,
        val step: SegmentStep
    ) : TimerState() {

        fun updateTime(timeInSeconds: Long): Counting {
            return copy(
                step = step.copy(
                    count = step.count.copy(timeInSeconds = timeInSeconds)
                )
            )
        }

        fun toggleTimeRunning(): Counting {
            return copy(step = step.copy(count = step.count.copy(isRunning = !step.count.isRunning)))
        }

        fun restartTime(): Counting {
            return copy(step = step.copy(count = Count.IDLE.copy(timeInSeconds = runningSegment.timeInSeconds)))
        }

        fun next(): Counting {
            return when (step.type) {
                SegmentStepType.STARTING -> nextSegmentStep()
                SegmentStepType.IN_PROGRESS -> nextSegment()
            }
        }

        private fun nextSegment(): Counting {
            val indexRunningSegment = routine.segments.indexOf(runningSegment)
            val segment = routine.segments[indexRunningSegment + 1]
            return copy(
                runningSegment = segment,
                step = SegmentStep(
                    count = Count.start(routine.startTimeOffsetInSeconds),
                    type = SegmentStepType.STARTING
                )
            )
        }

        private fun nextSegmentStep(): Counting {
            return copy(
                step = SegmentStep(
                    count = Count.start(runningSegment.timeInSeconds),
                    type = SegmentStepType.IN_PROGRESS
                )
            )
        }
    }
}