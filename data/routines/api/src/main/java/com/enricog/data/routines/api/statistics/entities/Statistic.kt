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
    val type: Type,
    val effectiveTime: Seconds
) {

    init {
        require(effectiveTime >= 0.seconds) {
            "effectiveTime must be positive"
        }
    }

    enum class Type {
        ROUTINE_COMPLETED, ROUTINE_ABORTED
    }

    companion object {
        fun create(routine: Routine): Statistic {
            val now = OffsetDateTime.now()
            return Statistic(
                id = ID.new(),
                routineId = routine.id,
                createdAt = now,
                type = Type.ROUTINE_ABORTED,
                effectiveTime = 0.seconds
            )
        }
    }
}
