package com.enricog.entities

data class Segment(
    val id: Int,
    val name: String,
    val timeInSeconds: Int,
    val type: TimeType
)