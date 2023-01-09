package com.enricog.features.routines.list.usecase

import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import com.enricog.entities.Rank
import javax.inject.Inject

internal class DuplicateRoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {
    suspend operator fun invoke(routine: Routine) {
        var segmentRankTop: Rank? = null
        val duplicatedSegments = routine.segments.map { segment ->
            val rank = segmentRankTop?.let { Rank.calculateBottom(it) } ?: Rank.calculateFirst()
            segmentRankTop = rank
            segment.copy(id = ID.new(), rank = rank)
        }

        val duplicatedRoutine = routine.copy(
            id = ID.new(),
            segments = duplicatedSegments
        )
        routineDataSource.create(routine = duplicatedRoutine)
    }
}