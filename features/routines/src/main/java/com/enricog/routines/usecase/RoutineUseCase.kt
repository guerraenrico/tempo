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
        return if (routineId >= 0) {
            routineDataSource.get(routineId)
        } else {
            val now = OffsetDateTime.now()
            Routine(
                id = -1,
                name = "",
                startTimeOffsetInSeconds = 0,
                createdAt = now,
                updatedAt = now,
                segments = emptyList()
            )
        }
    }

    suspend fun save(routine: Routine) {
        var routineToSave = routine
        if (routine.isNew) {
            val now = OffsetDateTime.now()
            routineToSave = routine.copy(
                createdAt = now,
                updatedAt = now,
            )
        }

        routineDataSource.create(routine)
    }

    private val Routine.isNew: Boolean
        get() = id < 0
}