package com.enricog.features.routines.detail.summary.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import javax.inject.Inject

internal class MoveSegmentUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend operator fun invoke(routine: Routine, draggedSegmentId: ID, hoveredSegmentId: ID) {
        val currentIndex = routine.segments.indexOfFirst { it.id == draggedSegmentId }
            .takeIf { it >= 0 } ?: return
        val newIndex = routine.segments.indexOfFirst { it.id == hoveredSegmentId }
            .takeIf { it >= 0 } ?: return

        if (currentIndex == newIndex) {
            return
        }

        fun getRank(index: Int): Rank? = routine.segments.getOrNull(index)?.rank

        val (rankTop, rankBottom) = if (currentIndex > newIndex) {
            getRank(index = newIndex - 1) to getRank(index = newIndex)
        } else {
            getRank(index = newIndex) to getRank(index = newIndex + 1)
        }
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
