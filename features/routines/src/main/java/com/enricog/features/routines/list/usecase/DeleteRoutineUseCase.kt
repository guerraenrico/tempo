package com.enricog.features.routines.list.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import javax.inject.Inject

internal class DeleteRoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {
    suspend operator fun invoke(routine: Routine) {
        routineDataSource.delete(routine = routine)
    }
}