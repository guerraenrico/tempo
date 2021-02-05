package com.enricog.entities.routines

data class Segment(
    val id: Long,
    val name: String,
    val timeInSeconds: Long,
    val type: TimeType
) {
    companion object {
        val NEW: Segment
            get() {
                return Segment(
                    id = 0,
                    name = "",
                    timeInSeconds = 0,
                    type = TimeType.TIMER
                )
            }
    }

    val isNew: Boolean
        get() = id == 0L
}
