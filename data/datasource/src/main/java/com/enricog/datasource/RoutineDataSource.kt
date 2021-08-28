package com.enricog.datasource

import com.enricog.entities.ID
import com.enricog.entities.routines.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineDataSource {

    fun observeAll(): Flow<List<Routine>>

    fun observe(id: ID): Flow<Routine>

    suspend fun get(id: ID): Routine

    suspend fun create(routine: Routine): ID

    suspend fun update(routine: Routine): ID

    suspend fun delete(routine: Routine)
}
