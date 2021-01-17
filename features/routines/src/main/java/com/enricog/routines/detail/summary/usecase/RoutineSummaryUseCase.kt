package com.enricog.routines.detail.summary.usecase

import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RoutineSummaryUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {
    fun get(routineId: Long): Flow<Routine> {
        return routineDataSource.observe(routineId)
    }

    suspend fun update(routine: Routine) {
        routineDataSource.update(routine)
    }
}