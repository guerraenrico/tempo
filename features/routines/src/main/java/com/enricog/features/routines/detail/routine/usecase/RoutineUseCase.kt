package com.enricog.features.routines.detail.routine.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import javax.inject.Inject

internal class RoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend fun get(routineId: ID): Routine {
        return when {
            routineId.isNew -> Routine.NEW
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
