package com.enricog.features.routines.list.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import javax.inject.Inject

internal class MoveRoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend operator fun invoke(
        routines: List<Routine>,
        draggedRoutineId: ID,
        hoveredRoutineId: ID
    ) {
        val currentIndex = routines.indexOfFirst { it.id == draggedRoutineId }
            .takeIf { it >= 0 } ?: return
        val newIndex = routines.indexOfFirst { it.id == hoveredRoutineId }
            .takeIf { it >= 0 } ?: return

        if (currentIndex == newIndex) {
            return
        }

        fun getRank(index: Int): Rank? = routines.getOrNull(index)?.rank

        val (rankTop, rankBottom) = if (currentIndex > newIndex) {
            getRank(index = newIndex - 1) to getRank(index = newIndex)
        } else {
            getRank(index = newIndex) to getRank(index = newIndex + 1)
        }
        val newRank = Rank.calculate(rankTop = rankTop, rankBottom = rankBottom)
        val routine = routines
            .first { it.id == draggedRoutineId }
            .copy(rank = newRank)
        routineDataSource.update(routine)
    }
}
