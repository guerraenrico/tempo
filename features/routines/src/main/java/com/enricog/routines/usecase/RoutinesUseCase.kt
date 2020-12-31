package com.enricog.routines.usecase

import android.annotation.SuppressLint
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import javax.inject.Inject

internal class RoutinesUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    @SuppressLint("NewApi")
    suspend fun getAll(): List<Routine> {
        return routineDataSource.getAll()
    }
}