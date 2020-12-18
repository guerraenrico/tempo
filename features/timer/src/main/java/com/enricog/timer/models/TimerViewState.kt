package com.enricog.timer.models

import com.enricog.entities.routines.Segment

internal sealed class TimerViewState {

    object Idle : TimerViewState()

    data class Counting(
        val runningSegment: Segment,
        val step: SegmentStep
    ) : TimerViewState()
}