package com.enricog.data.routines.testing.entities

import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds

val Segment.Companion.EMPTY: Segment
    get() = Segment(
        id = 0.asID,
        name = "",
        time = 0.seconds,
        type = TimeType.TIMER,
        rank = Rank.from(value = "aaaaaa"),
        rounds = 1
    )
