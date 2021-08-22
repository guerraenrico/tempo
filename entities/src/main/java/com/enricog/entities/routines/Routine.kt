package com.enricog.entities.routines

import com.enricog.entities.Seconds
import com.enricog.entities.seconds
import java.time.OffsetDateTime

data class Routine(
    val id: Long,
    val name: String,
    val startTimeOffset: Seconds,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val segments: List<Segment>
) {

    init {
        require(value = startTimeOffset <= MAX_START_TIME_OFFSET) {
            "startTimeOffset value exceed the maximum value"
        }
        require(value = startTimeOffset >= 0.seconds) {
            "startTimeOffset must be positive"
        }
    }

    companion object {
        val NEW: Routine
            get() {
                val now = OffsetDateTime.now()
                return Routine(
                    id = 0,
                    name = "",
                    startTimeOffset = 0.seconds,
                    createdAt = now,
                    updatedAt = now,
                    segments = emptyList()
                )
            }

        val MAX_START_TIME_OFFSET = 60.seconds
    }

    val isNew: Boolean
        get() = id == 0L
}
