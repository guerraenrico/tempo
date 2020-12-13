package com.enricog.entities.routines

import java.time.OffsetDateTime

data class Routine(
    val id: Int,
    val name: String,
    val startTimeOffsetInSeconds: Int,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val segments: List<Segment>
)