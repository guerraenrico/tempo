package com.enricog.localdatasource.routine.model

import com.enricog.entities.asID
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import java.time.OffsetDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class InternalRoutineTest {

    @Test
    fun testMappingToInternal() {
        val now = OffsetDateTime.now()
        val routine = Routine(
            id = 1.asID,
            name = "name",
            startTimeOffset = 2.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 3.asID,
                    name = "name",
                    time = 4.seconds,
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

        val actual = routine.toInternal()

        assertEquals(expected, actual)
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
            id = 1.asID,
            name = "name",
            startTimeOffset = 2.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 3.asID,
                    name = "name",
                    time = 4.seconds,
                    type = TimeType.TIMER
                )
            )
        )

        val actual = internalRoutine.toEntity(internalSegments)

        assertEquals(expected, actual)
    }
}
