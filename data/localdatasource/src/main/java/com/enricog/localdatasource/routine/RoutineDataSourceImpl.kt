package com.enricog.localdatasource.routine

import android.annotation.SuppressLint
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import com.enricog.localdatasource.TempoDatabase
import com.enricog.localdatasource.routine.model.toInternal
import java.time.OffsetDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@SuppressLint("NewApi")
internal class RoutineDataSourceImpl @Inject constructor(
    private val database: TempoDatabase
) : RoutineDataSource {

    override fun observeAll(): Flow<List<Routine>> {
        return database.routineDao().observeAll()
            .map { list -> list.map { it.toEntity() } }
    }

    override fun observe(id: Long): Flow<Routine> {
        return database.routineDao().observe(id)
            .map { it.toEntity() }
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

        if (addedSegments.isNotEmpty()) {
            database.segmentDao().insert(*addedSegments.toTypedArray())
        }
        if (deletedSegments.isNotEmpty()) {
            database.segmentDao().delete(*deletedSegments.toTypedArray())
        }
        if (updatedSegments.isNotEmpty()) {
            database.segmentDao().update(*updatedSegments.toTypedArray())
        }

        val now = OffsetDateTime.now()
        val routineToUpdate = routine.copy(updatedAt = now)
        database.routineDao().update(routineToUpdate.toInternal())
        return routine.id
    }

    override suspend fun delete(routine: Routine) {
        val internalRoutine = routine.toInternal()
        val internalSegments = routine.segments.map { it.toInternal(routine.id) }
        database.segmentDao().delete(*internalSegments.toTypedArray())
        database.routineDao().delete(internalRoutine)
    }
}
