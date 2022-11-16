package com.enricog.features.timer.models

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

internal sealed class TimerViewState {

    object Idle : TimerViewState()

    data class Counting(
        val step: SegmentStep,
        @StringRes val stepTitleId: Int,
        val segmentName: String,
        val clockBackgroundColor: BackgroundColor,
        val isRoutineCompleted: Boolean
    ) : TimerViewState() {

        data class BackgroundColor(
            val foreground: Color,
            val ripple: Color?
        )
    }

    data class Error(val throwable: Throwable) : TimerViewState()
}
