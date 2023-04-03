package com.enricog.features.timer.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.ID
import javax.inject.Inject

internal class GetRoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend operator fun invoke(routineId: ID): Routine {
        return routineDataSource.get(routineId)
    }
}
