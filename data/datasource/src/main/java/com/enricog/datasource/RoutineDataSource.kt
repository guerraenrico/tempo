package com.enricog.datasource

import com.enricog.entities.routines.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineDataSource {

    fun observeAll(): Flow<List<Routine>>

    fun observe(id: Long): Flow<Routine>

    suspend fun get(id: Long): Routine

    suspend fun create(routine: Routine): Long

    suspend fun update(routine: Routine): Long

    suspend fun delete(routine: Routine)
}