package com.enricog.features.routines.detail.summary.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.ID
import javax.inject.Inject

internal class DeleteSegmentUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend operator fun invoke(routine: Routine, segmentId: ID) {
        val segments = routine.segments.filterNot { it.id == segmentId }
        val newRoutine = routine.copy(segments = segments)
        routineDataSource.update(newRoutine)
    }
}