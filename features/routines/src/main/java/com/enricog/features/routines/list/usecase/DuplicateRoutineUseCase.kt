package com.enricog.features.routines.list.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.entities.ID
import com.enricog.entities.Rank
import javax.inject.Inject

internal class DuplicateRoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {
    suspend operator fun invoke(routines: List<Routine>, routineId: ID) {
        val routineIndex = routines.indexOfFirst { it.id == routineId }.takeIf { it >= 0 } ?: return
        val bottomRoutine = routines.getOrNull(routineIndex + 1)
        val routine = routines[routineIndex]
        val routineRank = Rank.calculate(rankTop = routine.rank, rankBottom = bottomRoutine?.rank)

        val duplicatedRoutine = routine.copy(
            id = ID.new(),
            segments = duplicateSegments(segments = routine.segments),
            rank = routineRank
        )
        routineDataSource.create(routine = duplicatedRoutine)
    }

    private fun duplicateSegments(segments: List<Segment>): List<Segment> {
        var segmentRankTop: Rank? = null
        return segments.map { segment ->
            val rank = segmentRankTop?.let { Rank.calculateBottom(it) } ?: Rank.calculateFirst()
            segmentRankTop = rank
            segment.copy(id = ID.new(), rank = rank)
        }
    }
}