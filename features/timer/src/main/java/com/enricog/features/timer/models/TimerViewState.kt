package com.enricog.features.timer.models

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

internal sealed class TimerViewState {

    object Idle : TimerViewState()

    data class Counting(
        val timeInSeconds: Long,
        val isRunning: Boolean,
        @StringRes val stepTitleId: Int,
        val segmentName: String,
        val clockBackgroundColor: BackgroundColor,
        val isSoundEnabled: Boolean
    ) : TimerViewState() {

        data class BackgroundColor(
            val foreground: Color,
            val ripple: Color?
        )
    }

    object Completed: TimerViewState()

    data class Error(val throwable: Throwable) : TimerViewState()
}
