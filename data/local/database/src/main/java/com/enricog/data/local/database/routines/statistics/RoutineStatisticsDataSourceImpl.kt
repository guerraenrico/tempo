package com.enricog.data.local.database.routines.statistics

import com.enricog.core.entities.ID
import com.enricog.data.local.database.TempoDatabase
import com.enricog.data.local.database.routines.statistics.model.InternalStatistic
import com.enricog.data.local.database.routines.statistics.model.toInternal
import com.enricog.data.routines.api.statistics.RoutineStatisticsDataSource
import com.enricog.data.routines.api.statistics.entities.Statistic
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.time.Clock
import java.time.OffsetDateTime
import javax.inject.Inject

internal class RoutineStatisticsDataSourceImpl @Inject constructor(
    private val database: TempoDatabase
) : RoutineStatisticsDataSource {

    override fun observeStatistics(from: OffsetDateTime, to: OffsetDateTime): Flow<List<Statistic>> {
        return database.statisticDao().observeAll(from, to)
            .map { it.map(InternalStatistic::toEntity) }
    }

    override suspend fun getAll(): List<Statistic> {
        return database.statisticDao().getAll()
            .map(InternalStatistic::toEntity)
    }

    override suspend fun getAllByRoutine(routineId: ID): List<Statistic> {
        return database.statisticDao()
            .getAllByRoutine(routineId = routineId.toLong())
            .map(InternalStatistic::toEntity)
    }

    override suspend fun getAllByRoutine(routineId: ID, from: OffsetDateTime, to: OffsetDateTime): List<Statistic> {
        return database.statisticDao()
            .getAllByRoutine(
                routineId = routineId.toLong(),
                from = from,
                to = to
            )
            .map(InternalStatistic::toEntity)
    }

    override suspend fun create(statistic: Statistic): ID {
        val now = OffsetDateTime.now()
        val statisticToCreate = statistic.copy(createdAt = now)
        val routineId = withContext(NonCancellable) {
            database.statisticDao()
                .insert(statisticToCreate.toInternal())
                .first()
        }
        yield()
        return ID.from(routineId)
    }
}
