package com.enricog.timer.models

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

internal sealed class TimerViewState {

    object Idle : TimerViewState()

    data class Counting(
        val step: SegmentStep,
        @StringRes val stepTitleId: Int,
        val segmentName: String,
        val clockBackgroundColor: Color,
        val isRoutineCompleted: Boolean,
        val enableKeepScreenOn: Boolean
    ) : TimerViewState()
}