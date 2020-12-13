package com.enricog.entities.routines

data class Segment(
    val id: Int,
    val name: String,
    val timeInSeconds: Int,
    val type: TimeType
)