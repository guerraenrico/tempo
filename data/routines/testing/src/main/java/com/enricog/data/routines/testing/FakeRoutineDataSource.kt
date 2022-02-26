package com.enricog.data.routines.testing

import com.enricog.base.extensions.replace
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeRoutineDataSource(
    private val store: FakeStore<List<Routine>> = FakeStore(emptyList())
) : RoutineDataSource {

    override fun observeAll(): Flow<List<Routine>> {
        return store.asFlow()
    }

    override fun observe(id: ID): Flow<Routine> {
        return store.asFlow().map { l -> l.first { r -> r.id == id } }
    }

    override suspend fun get(id: ID): Routine {
        return store.get().first { it.id == id }
    }

    override suspend fun create(routine: Routine): ID {
        store.update { it + listOf(routine) }
        return routine.id
    }

    override suspend fun update(routine: Routine): ID {
        store.update { l -> l.replace(routine) { r -> r.id == routine.id } }
        return routine.id
    }

    override suspend fun delete(routine: Routine) {
        store.update { l -> l.filterNot { r -> r.id == routine.id } }
    }
}