package com.enricog.features.routines.detail.summary.usecase

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.statistics.RoutineStatisticsDataSource
import com.enricog.data.routines.api.statistics.entities.Statistic
import java.time.Clock
import javax.inject.Inject

internal class GetRoutineStatistic @Inject constructor(
    private val statisticsDataSource: RoutineStatisticsDataSource,
    private val clock: Clock
) {

    suspend operator fun invoke(routine: Routine): List<Statistic> {
        val (from, to) = routine.frequencyGoal?.period?.timeRange(clock = clock) ?: return emptyList()
        return statisticsDataSource.getAllByRoutine(
            routineId = routine.id,
            from = from,
            to = to
        )
    }
}