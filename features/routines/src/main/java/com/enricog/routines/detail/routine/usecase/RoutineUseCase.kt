package com.enricog.routines.detail.routine.usecase

import android.annotation.SuppressLint
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import javax.inject.Inject

@SuppressLint("NewApi")
internal class RoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend fun get(routineId: Long): Routine {
        return if (routineId == 0L) {
            Routine.NEW
        } else {
            routineDataSource.get(routineId)
        }
    }

    suspend fun save(routine: Routine): Long {
        return if (routine.id == 0L) {
            routineDataSource.create(routine)
        } else {
            routineDataSource.update(routine)
        }
    }
}
