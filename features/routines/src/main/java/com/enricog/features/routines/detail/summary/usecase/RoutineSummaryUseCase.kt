package com.enricog.features.routines.detail.summary.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.entities.ID
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RoutineSummaryUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {
    fun get(routineId: ID): Flow<Routine> {
        return routineDataSource.observe(routineId)
    }

    suspend fun deleteSegment(routine: Routine, segment: Segment) {
        val segments = routine.segments.filterNot { it == segment }
        val newRoutine = routine.copy(segments = segments)
        routineDataSource.update(newRoutine)
    }
}
