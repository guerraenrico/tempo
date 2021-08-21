package com.enricog.entities.routines

import com.enricog.entities.Seconds
import com.enricog.entities.seconds

data class Segment(
    val id: Long,
    val name: String,
    val time: Seconds,
    val type: TimeType
) {

    init {
        require(value = time >= 0.seconds) {
            "time must be positive"
        }
    }

    companion object {
        val NEW: Segment
            get() {
                return Segment(
                    id = 0,
                    name = "",
                    time = 0.seconds,
                    type = TimeType.TIMER
                )
            }
    }

    val isNew: Boolean
        get() = id == 0L
}
