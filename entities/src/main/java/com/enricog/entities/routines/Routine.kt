package com.enricog.entities.routines

import java.time.OffsetDateTime

data class Routine(
    val id: Long,
    val name: String,
    val startTimeOffsetInSeconds: Long,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val segments: List<Segment>
)