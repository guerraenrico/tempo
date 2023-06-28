package com.enricog.data.routines.testing.statistics

import com.enricog.core.entities.ID
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.statistics.RoutineStatisticsDataSource
import com.enricog.data.routines.api.statistics.entities.Statistic

class FakeRoutineStatisticsDataSource(
    private val store: FakeStore<List<Statistic>>
) : RoutineStatisticsDataSource {

    override suspend fun getAll(): List<Statistic> {
        return store.get()
            .sortedByDescending { it.createdAt }
    }

    override suspend fun getAllByRoutine(routineId: ID): List<Statistic> {
        return store.get()
            .filter { it.routineId == routineId }
            .sortedByDescending { it.createdAt }
    }

    override suspend fun create(statistic: Statistic): ID {
        val newId = store.get().maxByOrNull { it.id }?.id
            ?.let { ID.from(value = it.toLong() + 1) }
            ?: ID.from(value = 1)
        val statisticToSave = statistic.copy(id = newId)
        store.update { it + listOf(statisticToSave) }
        return newId
    }
}