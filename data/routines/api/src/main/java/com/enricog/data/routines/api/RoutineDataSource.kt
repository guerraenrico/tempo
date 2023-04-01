package com.enricog.data.routines.api

import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.ID
import kotlinx.coroutines.flow.Flow

interface RoutineDataSource {

    fun observeAll(): Flow<List<Routine>>

    fun observe(id: ID): Flow<Routine>

    suspend fun getAll(): List<Routine>

    suspend fun get(id: ID): Routine

    suspend fun create(routine: Routine): ID

    suspend fun update(routine: Routine): ID

    suspend fun delete(routine: Routine)
}
