package com.enricog.features.timer.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.seconds

internal sealed class TimerState {

    object Idle : TimerState()

    data class Counting(
        val routine: Routine,
        val runningStep: SegmentStep,
        val steps: List<SegmentStep>,
        val isSoundEnabled: Boolean
    ) : TimerState() {

        private val runningStepIndex: Int
            get() = steps.indexOfFirst { it.id == runningStep.id }

        val runningSegment: Segment
            get() = runningStep.segment

        val nextSegment: Segment?
            get() = nextSegmentStep?.segment

        val nextSegmentStep: SegmentStep?
            get() = steps.getOrNull(index = runningStepIndex + 1)

        val previousSegmentStep: SegmentStep?
            get() = steps.getOrNull(index = runningStepIndex - 1)

        val isStepCountRunning: Boolean
            get() = runningStep.count.isRunning && !runningStep.count.isCompleted

        val isStepCountCompleted: Boolean
            get() = runningStep.count.isCompleted

        val isStepCountCompleting: Boolean
            get() = isStepCountRunning && !isStopwatchRunning &&
                runningStep.count.seconds <= STEP_COMPLETING_THRESHOLD

        val isRoutineCompleted: Boolean
            get() = nextSegment == null && runningStep.count.isCompleted

        val isStopwatchRunning: Boolean
            get() = runningSegment.type == TimeType.STOPWATCH &&
                runningStep.type == SegmentStepType.IN_PROGRESS &&
                isStepCountRunning

        private companion object {
            val STEP_COMPLETING_THRESHOLD = 5.seconds
        }
    }

    data class Error(val throwable: Throwable) : TimerState()
}
