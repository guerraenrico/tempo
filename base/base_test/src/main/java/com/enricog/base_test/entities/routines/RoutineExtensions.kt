package com.enricog.base_test.entities.routines

import android.annotation.SuppressLint
import com.enricog.entities.routines.Routine
import com.enricog.entities.seconds
import java.time.OffsetDateTime
import java.time.ZoneOffset

val Routine.Companion.EMPTY: Routine
    @SuppressLint("NewApi")
    get() = Routine(
        id = 0,
        name = "",
        startTimeOffset = 0.seconds,
        createdAt = OffsetDateTime.of(2020, 12, 20, 10, 10, 0, 0, ZoneOffset.UTC),
        updatedAt = OffsetDateTime.of(2020, 12, 20, 10, 10, 0, 0, ZoneOffset.UTC),
        segments = emptyList()
    )
