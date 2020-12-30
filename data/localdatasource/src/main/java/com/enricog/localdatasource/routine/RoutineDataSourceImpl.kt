package com.enricog.localdatasource.routine

import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import com.enricog.localdatasource.TempoDatabase
import com.enricog.localdatasource.routine.model.toInternal
import javax.inject.Inject

internal class RoutineDataSourceImpl @Inject constructor(
    private val database: TempoDatabase
) : RoutineDataSource {

    override suspend fun getAll(): List<Routine> {
        return database.routineDao().getAll().map { it.toEntity() }
    }

    override suspend fun get(id: Long): Routine {
        return database.routineDao().get(id).toEntity()
    }

    override suspend fun create(routine: Routine) {
        val routineId = database.routineDao()
            .insert(routine.toInternal())
            .first()

        val internalSegments = routine.segments.map { it.toInternal(routineId) }
        database.segmentDao().insert(*internalSegments.toTypedArray())
    }

    override suspend fun update(routine: Routine) {
        val currentRoutine = database.routineDao().get(routine.id)

        val internalSegments = routine.segments.map { it.toInternal(routine.id) }

        val addedSegments = internalSegments.filter { it.id == 0L }
        val deletedSegments = currentRoutine.segments.filter { currentSegment ->
            internalSegments.none { currentSegment.id == it.id }
        }
        val updatedSegments = internalSegments.filter { it.id > 0 }

        database.segmentDao().insert(*addedSegments.toTypedArray())
        database.segmentDao().delete(*deletedSegments.toTypedArray())
        database.segmentDao().update(*updatedSegments.toTypedArray())

        database.routineDao().update(routine.toInternal())
    }
}