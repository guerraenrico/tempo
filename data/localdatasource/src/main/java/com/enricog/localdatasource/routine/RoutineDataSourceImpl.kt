package com.enricog.localdatasource.routine

import android.annotation.SuppressLint
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.ID
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

    override fun observe(id: ID): Flow<Routine> {
        return database.routineDao().observe(id.toLong())
            .map { it.toEntity() }
    }

    override suspend fun get(id: ID): Routine {
        return database.routineDao().get(id.toLong()).toEntity()
    }

    override suspend fun create(routine: Routine): ID {
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
        return ID.from(routineId)
    }

    override suspend fun update(routine: Routine): ID {
        val savedRoutine = database.routineDao().get(routine.id.toLong())

        val addedSegments =  routine.segments
            .filter { it.isNew }
            .map { it.toInternal(routine.id.toLong()) }
        val deletedSegments = savedRoutine.segments.filter { savedSegment ->
            routine.segments.none { savedSegment.id == it.id.toLong() }
        }
        val updatedSegments = routine.segments
            .filter { !it.isNew }
            .map { it.toInternal(routine.id.toLong()) }

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
        database.routineDao().delete(internalRoutine)
    }
}
