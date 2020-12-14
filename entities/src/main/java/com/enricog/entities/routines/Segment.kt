package com.enricog.entities.routines

data class Segment(
    val id: Long,
    val name: String,
    val timeInSeconds: Long,
    val type: TimeType
)