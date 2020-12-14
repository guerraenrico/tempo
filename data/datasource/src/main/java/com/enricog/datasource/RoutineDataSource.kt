package com.enricog.datasource

import com.enricog.entities.routines.Routine

interface RoutineDataSource {

    suspend fun getAll(): List<Routine>

    suspend fun get(id: Long): Routine

    suspend fun create(routine: Routine)
}