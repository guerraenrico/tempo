package com.enricog.features.routines.list.usecase

import com.enricog.data.api.RoutineDataSource
import com.enricog.entities.routines.Routine
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

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
