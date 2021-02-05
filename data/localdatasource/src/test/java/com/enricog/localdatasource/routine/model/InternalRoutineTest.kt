package com.enricog.localdatasource.routine.model

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import java.time.OffsetDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class InternalRoutineTest {

    @Test
    fun testMappingToInternal() {
        val now = OffsetDateTime.now()
        val routine = Routine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 2,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 3,
                    name = "name",
                    timeInSeconds = 4,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 2,
            createdAt = now,
            updatedAt = now
        )

        val result = routine.toInternal()

        assertEquals(expected, result)
    }

    @Test
    fun testMappingToEntity() {
        val now = OffsetDateTime.now()
        val internalSegments = listOf(
            InternalSegment(
                id = 3,
                routineId = 1,
                name = "name",
                timeInSeconds = 4,
                type = TimeType.TIMER
            )
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 2,
            createdAt = now,
            updatedAt = now
        )
        val expected = Routine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 2,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 3,
                    name = "name",
                    timeInSeconds = 4,
                    type = TimeType.TIMER
                )
            )
        )

        val result = internalRoutine.toEntity(internalSegments)

        assertEquals(expected, result)
    }
}
