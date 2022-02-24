package com.enricog.data.localdatasource.routine.model

import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import org.junit.Assert.assertEquals
import org.junit.Test

class InternalSegmentTest {

    @Test
    fun testMappingToInternal() {
        val routineId = 1L
        val segment = Segment(
            id = 2.asID,
            name = "name",
            time = 3.seconds,
            type = TimeType.TIMER,
            rank = Rank.from("aaaaaa")
        )
        val expected = InternalSegment(
            id = 2,
            routineId = routineId,
            name = "name",
            timeInSeconds = 3,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )

        val result = segment.toInternal(routineId)

        assertEquals(expected, result)
    }

    @Test
    fun testMappingToEntity() {
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 2,
            name = "name",
            timeInSeconds = 3,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val expected = Segment(
            id = 1.asID,
            name = "name",
            time = 3.seconds,
            type = TimeType.TIMER,
            rank = Rank.from("aaaaaa")
        )

        val result = internalSegment.toEntity()

        assertEquals(expected, result)
    }
}
