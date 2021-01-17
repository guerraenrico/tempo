package com.enricog.entities.routines

import java.time.OffsetDateTime

data class Routine(
    val id: Long,
    val name: String,
    val startTimeOffsetInSeconds: Long,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val segments: List<Segment>
) {
    companion object {
        val NEW: Routine
            get() {
                val now = OffsetDateTime.now()
                return Routine(
                    id = 0,
                    name = "",
                    startTimeOffsetInSeconds = 0,
                    createdAt = now,
                    updatedAt = now,
                    segments = emptyList()
                )
            }
    }

    val isNew: Boolean
        get() = id == 0L
}