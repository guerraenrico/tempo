package com.enricog.features.timer.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.core.entities.Seconds
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.api.theme.entities.TimerTheme
import java.time.Clock
import java.time.Duration
import java.time.OffsetDateTime

internal sealed class TimerState {

    object Idle : TimerState()

    data class Counting(
        val timerTheme: TimerTheme,
        val timerSettings: TimerSettings,
        val routine: Routine,
        val runningStep: SegmentStep,
        val steps: List<SegmentStep>,
        val startedAt: OffsetDateTime,
        val skipCount: Int
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

        val isRoutineCompleted: Boolean
            get() = nextSegment == null && runningStep.count.isCompleted

        val isStopwatchRunning: Boolean
            get() = runningSegment.type == TimeType.STOPWATCH &&
                runningStep.type == SegmentStepType.IN_PROGRESS &&
                isStepCountRunning

        fun effectiveTotalSeconds(clock: Clock): Seconds {
            val difference = Duration.between(startedAt, OffsetDateTime.now(clock))
            return Seconds.from(difference.seconds)
        }
    }

    data class Error(val throwable: Throwable) : TimerState()
}
