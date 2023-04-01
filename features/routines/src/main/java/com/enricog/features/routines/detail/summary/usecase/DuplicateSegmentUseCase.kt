package com.enricog.features.routines.detail.summary.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import javax.inject.Inject

internal class DuplicateSegmentUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend operator fun invoke(routine: Routine, segmentId: ID) {
        val segmentIndex = routine.segments.indexOfFirst { it.id == segmentId }
            .takeIf { it >= 0 } ?: return
        val bottomSegment = routine.segments.getOrNull(segmentIndex + 1)
        val segment = routine.segments[segmentIndex]
        val segmentRank = Rank.calculate(rankTop = segment.rank, rankBottom = bottomSegment?.rank)

        val duplicatedSegment = segment.copy(
            id = ID.new(),
            rank = segmentRank
        )
        val updatedRoutine = routine.copy(segments = routine.segments + duplicatedSegment)
        routineDataSource.update(routine = updatedRoutine)
    }
}