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
    }

    companion object {
        val NEW: Routine
            get() {
                return Routine(
                    id = 0,
                    name = "",
                    startTimeOffset = 0.seconds,
                    createdAt = OffsetDateTime.MAX,
                    updatedAt = OffsetDateTime.MAX,
                    segments = emptyList()
                )
            }

        val MAX_START_TIME_OFFSET = 60.seconds
    }

    val isNew: Boolean
        get() = id == 0L
}
