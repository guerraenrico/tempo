package com.enricog.features.routines.detail.routine.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import com.enricog.entities.Rank
import javax.inject.Inject

internal class RoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend fun get(routineId: ID): Routine {
        return when {
            routineId.isNew -> {
                val firstRoutine = routineDataSource.getAll().firstOrNull()
                val rank = if (firstRoutine == null) Rank.calculateFirst() else
                    Rank.calculateTop(rank = firstRoutine.rank)
                Routine.create(rank = rank)
            }
            else -> routineDataSource.get(routineId)
        }
    }

    suspend fun save(routine: Routine): ID {
        return when {
            routine.id.isNew -> routineDataSource.create(routine)
            else -> routineDataSource.update(routine)
        }
    }
}
