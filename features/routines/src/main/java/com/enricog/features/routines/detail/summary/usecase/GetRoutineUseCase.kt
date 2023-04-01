package com.enricog.features.routines.detail.summary.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.ID
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetRoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    operator fun invoke(routineId: ID): Flow<Routine> {
        return routineDataSource.observe(id = routineId)
    }
}