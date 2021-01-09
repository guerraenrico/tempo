package com.enricog.routines.list.usecase

import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import javax.inject.Inject

internal class RoutinesUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend fun getAll(): List<Routine> {
        return routineDataSource.getAll()
    }

    suspend fun delete(routine: Routine) {
        routineDataSource.delete(routine)
    }
}