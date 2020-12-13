package com.enricog.localdatasource.routine

import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import javax.inject.Inject

class RoutineDataSourceImpl @Inject constructor() : RoutineDataSource {

    override suspend fun getAll(): List<Routine> {

    }

    override suspend fun get(id: Int): Routine {

    }
}