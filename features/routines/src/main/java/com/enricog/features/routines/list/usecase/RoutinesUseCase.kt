package com.enricog.features.routines.list.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
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
