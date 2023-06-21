package com.enricog.data.routines.api.statistics.entities

import com.enricog.core.entities.ID
import com.enricog.core.entities.Seconds
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.Routine
import java.time.OffsetDateTime

data class Statistic(
    val id: ID,
    val routineId: ID,
    val createdAt: OffsetDateTime,
    val completionEffectiveTime: Seconds
) {

    init {
        require(completionEffectiveTime >= 0.seconds) {
            "completionEffectiveTime must be positive"
        }
    }

    val isNew: Boolean
        get() = id.isNew

    companion object {
        fun create(routine: Routine): Statistic {
            val now = OffsetDateTime.now()
            return Statistic(
                id = ID.new(),
                routineId = routine.id,
                createdAt = now,
                completionEffectiveTime = 0.seconds
            )
        }
    }
}
