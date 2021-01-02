package com.enricog.timer.usecase

import android.annotation.SuppressLint
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import javax.inject.Inject

internal class TimerUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    @SuppressLint("NewApi")
    suspend fun get(routineId: Long): Routine {
        return routineDataSource.get(routineId)
    }

}