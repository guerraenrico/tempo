package com.enricog.timer.models

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType

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

        val isLastSegment: Boolean
            get() = routine.segments.indexOf(runningSegment) == routine.segments.size - 1

        val isStopwatchRunning: Boolean
            get() = runningSegment.type == TimeType.STOPWATCH &&
                    step.type == SegmentStepType.IN_PROGRESS &&
                    isCountRunning

        fun progressTime(): Counting {
            val goal = when (runningSegment.type) {
                TimeType.TIMER, TimeType.REST -> 0L
                TimeType.STOPWATCH -> runningSegment.timeInSeconds
            }
            val progress = when {
                step.type == SegmentStepType.STARTING -> -1L
                runningSegment.type == TimeType.TIMER || runningSegment.type == TimeType.REST -> -1L
                runningSegment.type == TimeType.STOPWATCH -> 1L
                else -> throw IllegalStateException("progress state not handled")
            }
            val timeInSeconds = step.count.timeInSeconds + progress
            val isCompleted = goal == timeInSeconds
            return copy(
                step = step.copy(
                    count = step.count.copy(
                        timeInSeconds = timeInSeconds,
                        isCompleted = isCompleted
                    )
                )
            )
        }

        fun toggleTimeRunning(): Counting {
            return copy(step = step.copy(count = step.count.copy(isRunning = !step.count.isRunning)))
        }

        fun restartTime(): Counting {
            return copy(step = step.copy(count = Count.idle(timeInSeconds = runningSegment.timeInSeconds)))
        }

        fun completeCount(): Counting {
            return copy(
                step = step.copy(
                    count = step.count.copy(isCompleted = true)
                )
            )
        }

        fun nextStep(): Counting {
            return when (step.type) {
                SegmentStepType.STARTING -> nextSegmentStep()
                SegmentStepType.IN_PROGRESS -> nextSegment()
            }
        }

        private fun nextSegmentStep(): Counting {
            return copy(
                step = SegmentStep(
                    count = Count.idle(runningSegment.timeInSeconds),
                    type = SegmentStepType.IN_PROGRESS
                )
            )
        }

        private fun nextSegment(): Counting {
            val indexRunningSegment = routine.segments.indexOf(runningSegment)
            val segment = routine.segments[indexRunningSegment + 1]
            val type = if (segment.type != TimeType.REST) {
                SegmentStepType.STARTING
            } else {
                SegmentStepType.IN_PROGRESS
            }
            return copy(
                runningSegment = segment,
                step = SegmentStep(
                    count = Count.idle(routine.startTimeOffsetInSeconds),
                    type = type
                )
            )
        }

    }
}