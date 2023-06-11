package com.enricog.features.timer

import com.enricog.data.routines.api.entities.TimeType
import com.enricog.core.entities.Seconds
import com.enricog.core.entities.seconds
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerState
import com.enricog.libraries.sound.api.SoundPlayer
import javax.inject.Inject

internal class TimerSoundPlayer @Inject constructor(
    private val soundPlayer: SoundPlayer
) : SoundPlayer by soundPlayer {

    fun playFrom(state: TimerState) {
        if (state !is TimerState.Counting) return
        if (!state.timerSettings.soundEnabled) return

        val soundToPlay = when {
            state.isRestStarted -> REST_SPEECH
            state.isPreparationStarted -> PREPARE_SPEECH
            state.isSegmentStarted -> GO_SPEECH

            state.isRoutineCompleted -> ROUTINE_COMPLETE_SOUND
            state.isStepCountCompleting -> COUNT_DOWN_SOUND
            state.isStepCountCompleted -> COUNT_DOWN_COMPLETED_SOUND

            state.isStepCountAt(15.seconds) -> SECS_15_SPEECH
            state.isStepCountAt(60.seconds) -> MIN_1_SPEECH
            state.isStepCountAt(300.seconds) -> MIN_5_SPEECH
            state.isStepCountAt(600.seconds) -> MIN_10_SPEECH
            state.isStepCountAt(900.seconds) -> MIN_15_SPEECH
            state.isStepCountAt(1800.seconds) -> MIN_30_SPEECH
            state.isStepCountAt(2700.seconds) -> MIN_45_SPEECH

            else -> {
                soundPlayer.keepAlive(COUNT_DOWN_SOUND)
                return
            }
        }

        soundPlayer.play(soundResId = soundToPlay)
    }

    private val TimerState.Counting.isStepCountCompleting: Boolean
        get() = isStepCountRunning && !isStepCountCompleted && !isStopwatchRunning &&
            runningStep.count.seconds <= STEP_COMPLETING_THRESHOLD

    private val TimerState.Counting.isRestStarted: Boolean
        get() = runningSegment.type == TimeType.REST &&
            runningStep.type == SegmentStepType.IN_PROGRESS &&
            isStepCountRunning && runningStep.count.seconds == runningSegment.time

    private val TimerState.Counting.isPreparationStarted: Boolean
        get() = runningStep.type == SegmentStepType.PREPARATION &&
            isStepCountRunning && runningStep.count.seconds == routine.preparationTime

    private val TimerState.Counting.isSegmentStarted: Boolean
        get() = (runningSegment.type == TimeType.TIMER || runningSegment.type == TimeType.STOPWATCH) &&
            runningStep.type == SegmentStepType.IN_PROGRESS &&
            isStepCountRunning && runningStep.count.seconds == runningSegment.time

    private fun TimerState.Counting.isStepCountAt(seconds: Seconds): Boolean {
        return isStepCountRunning && !isStopwatchRunning &&
            runningStep.count.seconds == seconds
    }

    private companion object {
        val STEP_COMPLETING_THRESHOLD = 5.seconds

        val COUNT_DOWN_SOUND = R.raw.sound_count_down
        val COUNT_DOWN_COMPLETED_SOUND = R.raw.sound_count_down_end
        val ROUTINE_COMPLETE_SOUND = R.raw.sound_routine_complete

        val REST_SPEECH = R.raw.speech_rest
        val PREPARE_SPEECH = R.raw.speech_prepare
        val GO_SPEECH = R.raw.speech_go

        val SECS_15_SPEECH = R.raw.speech_15_secs
        val MIN_1_SPEECH = R.raw.speech_1_min
        val MIN_5_SPEECH = R.raw.speech_5_mins
        val MIN_10_SPEECH = R.raw.speech_10_mins
        val MIN_15_SPEECH = R.raw.speech_15_mins
        val MIN_30_SPEECH = R.raw.speech_30_mins
        val MIN_45_SPEECH = R.raw.speech_45_mins
    }
}