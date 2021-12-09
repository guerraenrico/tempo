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

    val isNew: Boolean
        get() = id.isNew

    companion object {
        fun create(rank: Rank): Segment {
            return Segment(
                id = ID.new(),
                name = "",
                time = 0.seconds,
                type = TimeType.TIMER,
                rank = rank
            )
        }
    }
}

fun List<Segment>.sortedByRank(): List<Segment> {
    return sortedBy { it.rank }
}

fun List<Segment>.sortedByRankDescending(): List<Segment> {
    return sortedByDescending { it.rank }
}
