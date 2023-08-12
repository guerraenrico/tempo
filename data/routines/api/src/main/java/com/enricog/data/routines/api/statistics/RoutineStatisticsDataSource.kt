package com.enricog.data.routines.api.statistics

import com.enricog.core.entities.ID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.statistics.entities.Statistic
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

interface RoutineStatisticsDataSource {

    fun observeStatistics(from: OffsetDateTime, to: OffsetDateTime): Flow<List<Statistic>>

    suspend fun getAll(): List<Statistic>

    suspend fun getAllByRoutine(routineId: ID): List<Statistic>

    suspend fun create(statistic: Statistic): ID
}