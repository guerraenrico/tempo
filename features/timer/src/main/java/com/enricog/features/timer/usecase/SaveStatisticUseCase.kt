package com.enricog.features.timer.usecase

import com.enricog.core.coroutines.scope.ApplicationCoroutineScope
import com.enricog.core.entities.Seconds
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.statistics.RoutineStatisticsDataSource
import com.enricog.data.routines.api.statistics.entities.Statistic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class SaveStatisticUseCase @Inject constructor(
    @ApplicationCoroutineScope private val scope: CoroutineScope,
    private val routineStatisticsDataSource: RoutineStatisticsDataSource
) {

    operator fun invoke(routine: Routine, type: Statistic.Type, effectiveTime: Seconds) {
        scope.launch {
            val statistic = Statistic.create(routine = routine)
                .copy(type = type, effectiveTime = effectiveTime)
            routineStatisticsDataSource.create(statistic = statistic)
        }
    }
}
