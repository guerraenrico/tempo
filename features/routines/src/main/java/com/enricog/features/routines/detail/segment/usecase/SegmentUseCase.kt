package com.enricog.features.routines.detail.segment.usecase

import com.enricog.base.extensions.replace
import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.entities.ID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import javax.inject.Inject

internal class SegmentUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend fun get(routineId: ID): Routine {
        return routineDataSource.get(routineId)
    }

    suspend fun save(routine: Routine, segment: Segment) {
        val segments = routine.segments
        val updatedSegments = if (segment.isNew) {
            buildList {
                addAll(segments)
                add(segment)
            }
        } else {
            segments.replace(segment) { it.id == segment.id }
        }
        val updatedRoutine = routine.copy(segments = updatedSegments)
        routineDataSource.update(updatedRoutine)
    }
}
