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

        val isRoutineCompleted: Boolean
            get() = routine.segments.indexOf(runningSegment) == routine.segments.size - 1 &&
                    step.type == SegmentStepType.IN_PROGRESS &&
                    step.count.isCompleted

        val isStopwatchRunning: Boolean
            get() = runningSegment.type == TimeType.STOPWATCH &&
                    step.type == SegmentStepType.IN_PROGRESS &&
                    isCountRunning

        fun progressTime(): Counting {
            val goal = when {
                step.type == SegmentStepType.STARTING -> 0L
                runningSegment.type == TimeType.STOPWATCH -> -1L
                else -> 0L
            }
            val progress = when (step.type) {
                SegmentStepType.STARTING -> -1L
                else -> runningSegment.type.progress
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
            val timeInSeconds = when (step.type) {
                SegmentStepType.STARTING -> routine.startTimeOffsetInSeconds
                SegmentStepType.IN_PROGRESS -> runningSegment.timeInSeconds
            }
            return copy(step = step.copy(count = Count.idle(timeInSeconds = timeInSeconds)))
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
                    count = Count.start(runningSegment.timeInSeconds),
                    type = SegmentStepType.IN_PROGRESS
                )
            )
        }

        private fun nextSegment(): Counting {
            val indexRunningSegment = routine.segments.indexOf(runningSegment)
            val segment = routine.segments[indexRunningSegment + 1]
            val (type, timeInSeconds) = if (segment.type != TimeType.REST) {
                SegmentStepType.STARTING to routine.startTimeOffsetInSeconds
            } else {
                SegmentStepType.IN_PROGRESS to segment.timeInSeconds
            }
            return copy(
                runningSegment = segment,
                step = SegmentStep(
                    count = Count.start(timeInSeconds),
                    type = type
                )
            )
        }

    }
}