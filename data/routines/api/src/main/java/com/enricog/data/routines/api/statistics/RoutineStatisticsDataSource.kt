package com.enricog.data.routines.api.statistics

import com.enricog.core.entities.ID
import com.enricog.data.routines.api.statistics.entities.Statistic

interface RoutineStatisticsDataSource {

    suspend fun getAll(): List<Statistic>

    suspend fun getAllByRoutine(routineId: ID): List<Statistic>

    suspend fun create(statistic: Statistic): ID
}