package com.enricog.base_test.entities.routines

import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds

val Segment.Companion.EMPTY: Segment
    get() = Segment(
        id = 0.asID,
        name = "",
        time = 0.seconds,
        type = TimeType.TIMER,
        rank = Rank.from(value = "aaaaaa")
    )
