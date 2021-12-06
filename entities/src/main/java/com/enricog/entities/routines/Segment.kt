package com.enricog.entities.routines

import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.Seconds
import com.enricog.entities.seconds

data class Segment(
    val id: ID,
    val name: String,
    val time: Seconds,
    val type: TimeType,
    val rank: Rank
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
                    id = ID.new(),
                    name = "",
                    time = 0.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.NOT_SET
                )
            }
    }

    val isNew: Boolean
        get() = id.isNew
}
