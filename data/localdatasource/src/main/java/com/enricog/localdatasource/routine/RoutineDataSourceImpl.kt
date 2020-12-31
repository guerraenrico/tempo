package com.enricog.localdatasource.routine

import android.annotation.SuppressLint
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import com.enricog.localdatasource.TempoDatabase
import com.enricog.localdatasource.routine.model.toInternal
import java.time.OffsetDateTime
import javax.inject.Inject

@SuppressLint("NewApi")
internal class RoutineDataSourceImpl @Inject constructor(
    private val database: TempoDatabase
) : RoutineDataSource {

    override suspend fun getAll(): List<Routine> {
        return database.routineDao().getAll().map { it.toEntity() }
    }

    override suspend fun get(id: Long): Routine {
        return database.routineDao().get(id).toEntity()
    }

    override suspend fun create(routine: Routine): Long {
        val now = OffsetDateTime.now()
        val routineToCreate = routine.copy(
            createdAt = now,
            updatedAt = now,
        )
        val routineId = database.routineDao()
            .insert(routineToCreate.toInternal())
            .first()

        val internalSegments = routine.segments.map { it.toInternal(routineId) }
        database.segmentDao().insert(*internalSegments.toTypedArray())
        return routineId
    }

    override suspend fun update(routine: Routine): Long {
        val currentRoutine = database.routineDao().get(routine.id)
        val currentInternalSegments = routine.segments.map { it.toInternal(routine.id) }

        val addedSegments = currentInternalSegments.filter { it.id == 0L }
        val deletedSegments = currentRoutine.segments.filter { currentSegment ->
            currentInternalSegments.none { currentSegment.id == it.id }
        }
        val updatedSegments = currentInternalSegments.filter { it.id > 0 }

        database.segmentDao().insert(*addedSegments.toTypedArray())
        database.segmentDao().delete(*deletedSegments.toTypedArray())
        database.segmentDao().update(*updatedSegments.toTypedArray())

        val now = OffsetDateTime.now()
        val routineToUpdate = routine.copy(updatedAt = now)
        database.routineDao().update(routineToUpdate.toInternal())
        return routine.id
    }
}