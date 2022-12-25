package com.enricog.features.routines.list.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import com.enricog.entities.Rank
import javax.inject.Inject

internal class MoveRoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend operator fun invoke(routines: List<Routine>, draggedRoutineId: ID, hoveredRoutineId: ID?) {
        val currentIndex = routines.indexOfFirst { it.id == draggedRoutineId }
        val newIndex = routines.indexOfFirst { it.id == hoveredRoutineId }

        if (currentIndex == newIndex || currentIndex < 0) {
            return
        }

        val rankTop = routines.getOrNull(newIndex)?.rank
        val rankBottom = routines.getOrNull(newIndex + 1)?.rank
        val newRank = Rank.Companion.calculate(rankTop = rankTop, rankBottom = rankBottom)
        val routine = routines
            .first { it.id == draggedRoutineId }
            .copy(rank = newRank)
        routineDataSource.update(routine)
    }
}
