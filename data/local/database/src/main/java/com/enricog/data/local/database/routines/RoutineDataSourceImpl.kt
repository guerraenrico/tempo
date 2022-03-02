package com.enricog.data.local.database.routines

import android.annotation.SuppressLint
import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.entities.ID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.local.database.TempoDatabase
import com.enricog.data.local.database.routines.model.InternalRoutineWithSegments
import com.enricog.data.local.database.routines.model.toInternal
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.time.OffsetDateTime
import javax.inject.Inject

@SuppressLint("NewApi")
internal class RoutineDataSourceImpl @Inject constructor(
    private val database: TempoDatabase
) : RoutineDataSource {

    override fun observeAll(): Flow<List<Routine>> {
        return database.routineDao().observeAll()
            .map { list -> list.map(InternalRoutineWithSegments::toEntity) }
    }

    override fun observe(id: ID): Flow<Routine> {
        return database.routineDao().observe(id.toLong())
            .map(InternalRoutineWithSegments::toEntity)
    }

    override suspend fun get(id: ID): Routine {
        return database.routineDao().get(id.toLong()).toEntity()
    }

    override suspend fun create(routine: Routine): ID {
        val now = OffsetDateTime.now()
        val routineToCreate = routine.copy(createdAt = now, updatedAt = now)
        val routineId = withContext(NonCancellable) {
            val id = database.routineDao()
                .insert(routineToCreate.toInternal())
                .first()
            val internalSegments = routine.segments.map { it.toInternal(id) }
            database.segmentDao().insert(*internalSegments.toTypedArray())
            return@withContext id
        }
        yield()
        return ID.from(routineId)
    }

    override suspend fun update(routine: Routine): ID {
        withContext(NonCancellable) {
            val savedRoutine = database.routineDao().get(routine.id.toLong())

            val addedSegments = routine.segments
                .filter { it.isNew }
                .map { it.toInternal(routine.id.toLong()) }
            val deletedSegments = savedRoutine.segments
                .filter { savedSegment ->
                    routine.segments.none { savedSegment.id == it.id.toLong() }
                }
            val updatedSegments = routine.segments
                .filterNot { it.isNew }
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
        }
        yield()
        return routine.id
    }

    override suspend fun delete(routine: Routine) {
        withContext(NonCancellable) {
            val internalRoutine = routine.toInternal()
            database.routineDao().delete(internalRoutine)
        }
        yield()
    }
}
