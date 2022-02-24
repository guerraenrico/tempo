package com.enricog.features.routines.detail.routine.usecase

import android.annotation.SuppressLint
import com.enricog.data.datasource.RoutineDataSource
import com.enricog.entities.ID
import com.enricog.entities.routines.Routine
import javax.inject.Inject

@SuppressLint("NewApi")
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
