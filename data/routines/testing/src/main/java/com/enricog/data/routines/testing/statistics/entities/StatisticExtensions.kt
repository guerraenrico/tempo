package com.enricog.data.routines.testing.statistics.entities

import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.statistics.entities.Statistic
import java.time.OffsetDateTime

val Statistic.Companion.EMPTY: Statistic
    get() = Statistic(
        id = 0.asID,
        routineId = 0.asID,
        createdAt = OffsetDateTime.MIN,
        type = Statistic.Type.ROUTINE_COMPLETED,
        effectiveTime = 0.seconds
    )
