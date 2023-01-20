package com.enricog.features.routines.list.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetRoutinesUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    operator fun invoke(): Flow<List<Routine>> {
        return routineDataSource.observeAll()
    }
}
