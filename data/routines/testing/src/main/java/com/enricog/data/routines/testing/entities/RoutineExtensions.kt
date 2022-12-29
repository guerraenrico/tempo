package com.enricog.data.routines.testing.entities

import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.seconds
import java.time.OffsetDateTime
import java.time.ZoneOffset

val Routine.Companion.EMPTY: Routine
    @Suppress("NewApi")
    get() = Routine(
        id = 0.asID,
        name = "",
        startTimeOffset = 0.seconds,
        createdAt = OffsetDateTime.of(2020, 12, 20, 10, 10, 0, 0, ZoneOffset.UTC),
        updatedAt = OffsetDateTime.of(2020, 12, 20, 10, 10, 0, 0, ZoneOffset.UTC),
        segments = emptyList(),
        rank = Rank.from(value = "aaaaaa")
    )
