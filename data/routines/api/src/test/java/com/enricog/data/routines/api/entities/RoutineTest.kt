package com.enricog.data.routines.api.entities

import com.enricog.data.routines.api.entities.Routine.Companion.MAX_START_TIME_OFFSET
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.seconds
import java.time.OffsetDateTime
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class RoutineTest {

    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    @Test
    fun `on instantiation should throw exception when start offset time is more than max`() {
        val startTimeOffset = MAX_START_TIME_OFFSET + 1.seconds

        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("startTimeOffset value exceed the maximum value")

        Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = startTimeOffset,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )
    }

    @Test
    fun `on instantiation should throw exception when start offset time is less than zero`() {
        val startTimeOffset = (-1).seconds

        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("startTimeOffset must be positive")

        Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = startTimeOffset,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )
    }

    @Suppress("UnusedDataClassCopyResult")
    @Test
    fun `on copy should throw exception when start offset time is more than max`() {
        val startTimeOffset = MAX_START_TIME_OFFSET + 1.seconds

        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("startTimeOffset value exceed the maximum value")

        val routine = Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = 50.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )
        routine.copy(startTimeOffset = startTimeOffset)
    }

    @Suppress("UnusedDataClassCopyResult")
    @Test
    fun `on copy should throw exception when start offset time is less that zero`() {
        val startTimeOffset = (-1).seconds

        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("startTimeOffset must be positive")

        val routine = Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = 50.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )
        routine.copy(startTimeOffset = startTimeOffset)
    }

    @Test
    fun `on instantiation should not throw any exception when all argument are valid`() {
        Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = MAX_START_TIME_OFFSET,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )
    }

    @Suppress("UnusedDataClassCopyResult")
    @Test
    fun `on copy should not throw any exception when all argument are valid`() {
        val routine = Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = 50.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )
        routine.copy(startTimeOffset = MAX_START_TIME_OFFSET)
    }
}
