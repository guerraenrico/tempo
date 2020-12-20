package com.enricog.base_test.entities.routines

import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType

val Segment.Companion.EMPTY: Segment
    get() = Segment(
        id = 0,
        name = "",
        timeInSeconds = 0,
        type = TimeType.TIMER
    )
