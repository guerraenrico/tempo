package com.enricog.timer.models

import androidx.compose.ui.graphics.Color
import com.enricog.entities.routines.Segment

internal sealed class TimerViewState {

    object Idle : TimerViewState()

    data class Counting(
        val runningSegment: Segment,
        val step: SegmentStep,
        val clockBackgroundColor: Color
    ) : TimerViewState()

    object Done : TimerViewState()
}