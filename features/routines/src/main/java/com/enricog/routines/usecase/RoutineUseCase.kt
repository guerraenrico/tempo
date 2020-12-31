package com.enricog.routines.usecase

import android.annotation.SuppressLint
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import java.time.OffsetDateTime
import javax.inject.Inject

@SuppressLint("NewApi")
internal class RoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    private val newRoutine: Routine
        get() {
            val now = OffsetDateTime.now()
            return Routine(
                id = 0,
                name = "",
                startTimeOffsetInSeconds = 0,
                createdAt = now,
                updatedAt = now,
                segments = emptyList()
            )
        }

    suspend fun get(routineId: Long): Routine {
        return if (routineId == 0L) {
            newRoutine
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