package com.enricog.features.routines.detail.summary.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import com.enricog.entities.Rank
import javax.inject.Inject

internal class MoveSegmentUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend operator fun invoke(routine: Routine, draggedSegmentId: ID, hoveredSegmentId: ID?) {
        val currentIndex = routine.segments.indexOfFirst { it.id == draggedSegmentId }
        val newIndex = routine.segments.indexOfFirst { it.id == hoveredSegmentId }

        if (currentIndex == newIndex || currentIndex < 0) {
            return
        }

        val rankTop = routine.segments.getOrNull(newIndex)?.rank
        val rankBottom = routine.segments.getOrNull(newIndex + 1)?.rank
        val newRank = Rank.calculate(rankTop = rankTop, rankBottom = rankBottom)
        val segments = routine.segments
            .map {
                if (it.id == draggedSegmentId) {
                    it.copy(rank = newRank)
                } else {
                    it
                }
            }
        routineDataSource.update(routine.copy(segments = segments))
    }
}
