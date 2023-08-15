package com.enricog.features.routines.list.usecase

import com.enricog.core.date.api.shiftToEndOfDay
import com.enricog.core.date.api.shiftToStartOfDay
import com.enricog.data.routines.api.statistics.RoutineStatisticsDataSource
import com.enricog.data.routines.api.statistics.entities.Statistic
import kotlinx.coroutines.flow.Flow
import java.time.Clock
import java.time.OffsetDateTime
import javax.inject.Inject

internal class GetLatestStatisticUseCase @Inject constructor(
    private val routineStatisticsDataSource: RoutineStatisticsDataSource,
    private val clock: Clock
) {

    operator fun invoke(): Flow<List<Statistic>> {
        val today = OffsetDateTime.now(clock).shiftToEndOfDay()
        val oneMonthAgo = today.minusMonths(1).shiftToStartOfDay()
        return routineStatisticsDataSource.observeStatistics(
            from = oneMonthAgo,
            to = today
        )
    }
}