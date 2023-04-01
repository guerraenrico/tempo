package com.enricog.data.routines.testing

import com.enricog.base.extensions.replace
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.sortedByRank
import com.enricog.core.entities.ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeRoutineDataSource(
    private val store: FakeStore<List<Routine>>
) : RoutineDataSource {

    override fun observeAll(): Flow<List<Routine>> {
        return store.asFlow()
            .map { it.orderSegments() }
            .map { it.sortedByRank() }
    }

    override fun observe(id: ID): Flow<Routine> {
        return store.asFlow()
            .map { l -> l.first { r -> r.id == id } }
            .map { it.orderSegments() }
    }

    override suspend fun getAll(): List<Routine> {
        return store.get().sortedByRank()
    }

    override suspend fun get(id: ID): Routine {
        return store.get()
            .first { it.id == id }
            .orderSegments()
    }

    override suspend fun create(routine: Routine): ID {
        val newId = store.get().maxByOrNull { it.id }?.id
            ?.let { ID.from(value = it.toLong() + 1) }
            ?: ID.from(value = 1)
        val routineToSave = routine.copy(
            id = newId,
            segments = routine.segments.mapIndexed { index, segment ->
                segment.copy(id = ID.from(value = index.toLong() + 1))
            }
        )
        store.update { it + listOf(routineToSave) }
        return newId
    }

    override suspend fun update(routine: Routine): ID {
        var maxId = routine.segments.maxByOrNull { it.id }?.id
            ?: ID.from(value = 1)
        val routineToSave = routine.copy(
            segments = routine.segments.map { segment ->
                if (segment.id.isNew) {
                    maxId = ID.from(value = maxId.toLong() + 1)
                    segment.copy(id = maxId)
                } else {
                    segment
                }
            }
        )
        store.update { l -> l.replace(routineToSave) { r -> r.id == routineToSave.id } }
        return routine.id
    }

    override suspend fun delete(routine: Routine) {
        store.update { l -> l.filterNot { r -> r.id == routine.id } }
    }

    private fun List<Routine>.orderSegments(): List<Routine> {
        return map { it.orderSegments() }
    }

    private fun Routine.orderSegments(): Routine {
        return copy(segments = segments.sortedByRank())
    }
}