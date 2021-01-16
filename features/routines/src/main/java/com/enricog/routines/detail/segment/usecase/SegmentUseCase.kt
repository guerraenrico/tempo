package com.enricog.routines.detail.segment.usecase

import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import javax.inject.Inject

internal class SegmentUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend fun get(routineId: Long): Routine {
        return routineDataSource.get(routineId)
    }

    suspend fun save(routine: Routine, segment: Segment) {
        val segments = routine.segments
        val updatedSegments = if (segment.id == 0L) {
            buildList {
                addAll(segments)
                add(segment)
            }
        } else {
            segments.map {
                if (it.id == segment.id) {
                    segment
                } else {
                    it
                }
            }
        }
        val updatedRoutine = routine.copy(segments = updatedSegments)
        routineDataSource.update(updatedRoutine)
    }

}