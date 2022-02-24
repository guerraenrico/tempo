package com.enricog.features.routines.detail.summary.usecase

import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.Rank
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import javax.inject.Inject

internal class MoveSegmentUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    suspend operator fun invoke(routine: Routine, segment: Segment, hoveredSegment: Segment?) {
        val itemIndex = routine.segments.indexOf(segment)
        val newIndex = routine.segments.indexOf(hoveredSegment)

        if (itemIndex == newIndex || itemIndex < 0) {
            return
        }

        val rank1 = routine.segments.getOrNull(newIndex)?.rank
        val rank2 = routine.segments.getOrNull(newIndex + 1)?.rank
        val newRank = Rank.calculate(rank1 = rank1, rank2 = rank2)
        val segments = routine.segments
            .map {
                if (it.id == segment.id) {
                    it.copy(rank = newRank)
                } else {
                    it
                }
            }
        routineDataSource.update(routine.copy(segments = segments))
    }
}