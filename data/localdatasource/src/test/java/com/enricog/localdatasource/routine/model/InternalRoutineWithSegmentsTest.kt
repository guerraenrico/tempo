package com.enricog.localdatasource.routine.model

import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import java.time.OffsetDateTime
import org.junit.Assert
import org.junit.Test

class InternalRoutineWithSegmentsTest {

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
            startTimeOffset = 2.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 3,
                    name = "name",
                    time = 4.seconds,
                    type = TimeType.TIMER
                )
            )
        )
        val internalRoutineWithSegments = InternalRoutineWithSegments(
            routine = internalRoutine,
            segments = internalSegments
        )

        val result = internalRoutineWithSegments.toEntity()

        Assert.assertEquals(expected, result)
    }
}
