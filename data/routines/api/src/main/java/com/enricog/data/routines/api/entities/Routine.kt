package com.enricog.data.routines.api.entities

import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import com.enricog.core.entities.Seconds
import com.enricog.core.entities.seconds
import java.time.OffsetDateTime

data class Routine(
    val id: ID,
    val name: String,
    val preparationTime: Seconds,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val segments: List<Segment>,
    val rank: Rank,
    val rounds: Int
) {

    init {
        require(value = preparationTime <= MAX_PREPARATION_TIME) {
            "preparationTime value exceed the maximum value"
        }
        require(value = preparationTime >= 0.seconds) {
            "preparationTime must be positive"
        }
        require(value = rounds >= 1) {
            "rounds must be equals or more than 1"
        }
    }

    val isNew: Boolean
        get() = id.isNew

    val expectedTotalTime: Seconds
        get() {
            val segmentsTotalTime = segments.map { it.time }
                .reduce { acc, time -> acc + time }
            val preparationTotalTime = preparationTime * segments.count {
                it.type.requirePreparationTime
            }
            if (segmentsTotalTime == 0.seconds)
                return 0.seconds
            return segmentsTotalTime + preparationTotalTime
        }

    fun getNewSegmentRank(): Rank {
        return when {
            segments.isEmpty() -> Rank.calculateFirst()
            else -> Rank.calculateBottom(segments.last().rank)
        }
    }

    companion object {
        fun create(rank: Rank): Routine {
            val now = OffsetDateTime.now()
            return Routine(
                id = ID.new(),
                name = "",
                preparationTime = 0.seconds,
                createdAt = now,
                updatedAt = now,
                segments = emptyList(),
                rank = rank,
                rounds = 1
            )
        }

        val MAX_PREPARATION_TIME = 60.seconds
        const val MIN_NUM_ROUNDS = 1
    }
}

fun List<Routine>.sortedByRank(): List<Routine> {
    return sortedBy { it.rank }
}
