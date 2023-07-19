package com.enricog.data.routines.api.entities

import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import com.enricog.core.entities.Seconds
import com.enricog.core.entities.seconds

data class Segment(
    val id: ID,
    val name: String,
    val time: Seconds,
    val type: TimeType,
    val rank: Rank,
    val rounds: Int
) {

    init {
        require(value = time >= 0.seconds) {
            "time must be positive"
        }
        require(value = rounds >= 1) {
            "rounds must be equals or more than 1"
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
                rank = rank,
                rounds = MIN_NUM_ROUNDS
            )
        }

        const val MIN_NUM_ROUNDS = 1
    }
}

fun List<Segment>.sortedByRank(): List<Segment> {
    return sortedBy { it.rank }
}
