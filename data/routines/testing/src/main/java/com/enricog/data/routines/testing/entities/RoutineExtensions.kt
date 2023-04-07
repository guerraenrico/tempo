package com.enricog.data.routines.testing.entities

import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.Routine
import java.time.OffsetDateTime
import java.time.ZoneOffset

val Routine.Companion.EMPTY: Routine
    get() = Routine(
        id = 0.asID,
        name = "",
        preparationTime = 0.seconds,
        createdAt = OffsetDateTime.of(2020, 12, 20, 10, 10, 0, 0, ZoneOffset.UTC),
        updatedAt = OffsetDateTime.of(2020, 12, 20, 10, 10, 0, 0, ZoneOffset.UTC),
        segments = emptyList(),
        rank = Rank.from(value = "aaaaaa")
    )
