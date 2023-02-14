package com.enricog.features.timer

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.seconds
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import javax.inject.Inject

internal class TimerReducer @Inject constructor() {

    fun setup(state: TimerState, routine: Routine): TimerState {
        val steps = buildList {
            fun generateId() : () -> Int {
                var id = 0
                return { id.also { id++ } }
            }
            val getNextId = generateId()

            for (segment in routine.segments) {
                if (segment.type.requirePreparationTime && routine.preparationTime > 0.seconds) {
                    add(
                        SegmentStep(
                            id = getNextId(),
                            count = Count.idle(seconds = routine.preparationTime),
                            type = SegmentStepType.STARTING,
                            segment = segment
                        )
                    )
                }
                add(
                    SegmentStep(
                        id = getNextId(),
                        count = Count.idle(seconds = segment.time),
                        type = SegmentStepType.IN_PROGRESS,
                        segment = segment
                    )
                )
            }
        }
        return TimerState.Counting(
            routine = routine,
            runningStep = steps.first(),
            steps = steps,
            isSoundEnabled = if (state is TimerState.Counting) state.isSoundEnabled else true
        )
    }

    fun error(throwable: Throwable): TimerState {
        return TimerState.Error(throwable = throwable)
    }

    fun progressTime(state: TimerState): TimerState {
        if (state !is TimerState.Counting || !state.isStepCountRunning) return state

        val step = state.runningStep
        val runningSegment = state.runningSegment
        val goal = when {
            step.type == SegmentStepType.STARTING -> 0.seconds
            runningSegment.type == TimeType.STOPWATCH -> (-1).seconds
            else -> 0.seconds
        }
        val progress = when (step.type) {
            SegmentStepType.STARTING -> (-1).seconds
            else -> runningSegment.type.progress
        }
        val seconds = step.count.seconds + progress
        val isCompleted = goal == seconds
        return state.copy(
            runningStep = step.copy(
                count = step.count.copy(
                    seconds = seconds,
                    isCompleted = isCompleted
                )
            )
        )
    }

    fun toggleTimeRunning(state: TimerState): TimerState {
        if (state !is TimerState.Counting || state.isStepCountCompleted) return state

        val step = state.runningStep
        return if (state.isStopwatchRunning) {
            state.copy(runningStep = step.copy(count = step.count.copy(isCompleted = true)))
        } else {
            state.copy(runningStep = step.copy(count = step.count.copy(isRunning = !step.count.isRunning)))
        }
    }

    fun toggleSound(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        return state.copy(isSoundEnabled = !state.isSoundEnabled)
    }

    fun jumpStepBack(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        val currentResetTime = when (state.runningStep.type) {
            SegmentStepType.STARTING -> state.routine.preparationTime
            SegmentStepType.IN_PROGRESS -> state.runningSegment.time
        }

        val previousStep = state.previousSegmentStep
        return if (previousStep != null && state.runningStep.count.seconds == currentResetTime) {
            state.copy(runningStep = previousStep)
        } else {
            state.copy(runningStep = state.runningStep.copy(count = Count.idle(seconds = currentResetTime)))
        }
    }

    fun jumpStepNext(state: TimerState): TimerState {
        if (state !is TimerState.Counting) return state

        val nextStep = state.nextSegmentStep
        return if (nextStep != null) {
            state.copy(runningStep = nextStep)
        } else {
            state.copy(
                runningStep = state.runningStep.copy(
                    count = state.runningStep.count.copy(isCompleted = true)
                )
            )
        }
    }

    fun nextStep(state: TimerState): TimerState {
        if (
            state !is TimerState.Counting ||
            !state.isStepCountCompleted ||
            state.isRoutineCompleted
        ) return state

        val nextSegmentStep = requireNotNull(state.nextSegmentStep)
        return state.copy(runningStep = nextSegmentStep.copy(count = Count.start(nextSegmentStep.count.seconds)))
    }
}
