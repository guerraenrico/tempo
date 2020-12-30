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

    suspend fun get(routineId: Long): Routine {
        return if (routineId == 0L) {
            val now = OffsetDateTime.now()
            Routine(
                id = 0,
                name = "",
                startTimeOffsetInSeconds = 0,
                createdAt = now,
                updatedAt = now,
                segments = emptyList()
            )

        } else {
            routineDataSource.get(routineId)
        }
    }

    suspend fun save(routine: Routine) {
        var routineToSave = routine
        if (routine.id == 0L) {
            val now = OffsetDateTime.now()
            routineToSave = routine.copy(
                createdAt = now,
                updatedAt = now,
            )
        }

        routineDataSource.create(routine)
    }
}