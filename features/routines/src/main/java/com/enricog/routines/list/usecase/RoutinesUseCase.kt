package com.enricog.routines.list.usecase

import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RoutinesUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    fun getAll(): Flow<List<Routine>> {
        return routineDataSource.observeAll()
    }

    suspend fun delete(routine: Routine) {
        routineDataSource.delete(routine)
    }
}