package com.enricog.routines.detail.summary.usecase

import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

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
