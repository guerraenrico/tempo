package com.enricog.data.routines.api.entities

import com.enricog.data.routines.api.entities.Routine.Companion.MAX_START_TIME_OFFSET
import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.OffsetDateTime

class RoutineTest {

    @Test
    fun `on instantiation should throw exception when start offset time is more than max`() {
        val startTimeOffset = MAX_START_TIME_OFFSET + 1.seconds

        assertThrows(IllegalArgumentException::class.java) {
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
    }

    @Test
    fun `on init should throw exception when start offset time is less than zero`() {
        val startTimeOffset = (-1).seconds

        assertThrows(IllegalArgumentException::class.java) {
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
    }

    @Test
    @Suppress("UnusedDataClassCopyResult")
    fun `on copy should throw exception when start offset time is more than max`() {
        val startTimeOffset = MAX_START_TIME_OFFSET + 1.seconds

        val routine = Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = 50.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )

        assertThrows(IllegalArgumentException::class.java) {
            routine.copy(startTimeOffset = startTimeOffset)
        }
    }

    @Test
    @Suppress("UnusedDataClassCopyResult")
    fun `on copy should throw exception when start offset time is less that zero`() {
        val startTimeOffset = (-1).seconds

        val routine = Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = 50.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )
        assertThrows(IllegalArgumentException::class.java) {
            routine.copy(startTimeOffset = startTimeOffset)
        }
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

    @Test
    @Suppress("UnusedDataClassCopyResult")
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

    @Test
    fun `expected total time should be zero when routine has only stopwatch segment`() {
        val routine = Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = 30.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = listOf(
                Segment(
                    id = ID.new(),
                    name = "",
                    time = 0.seconds,
                    type = TimeType.STOPWATCH,
                    rank = Rank.from("aaaaaa")
                )
            ),
            rank = Rank.from("aaaaaa")
        )
        val expected = 0.seconds

        val actual = routine.expectedTotalTime

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `expected total time should include preparation time`() {
        val routine = Routine(
            id = 0.asID,
            name = "",
            startTimeOffset = 30.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = listOf(
                Segment(
                    id = ID.new(),
                    name = "",
                    time = 30.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from("aaaaaa")
                ),
                Segment(
                    id = ID.new(),
                    name = "",
                    time = 30.seconds,
                    type = TimeType.REST,
                    rank = Rank.from("bbbbbb")
                ),
                Segment(
                    id = ID.new(),
                    name = "",
                    time = 30.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from("aaaaaa")
                ),
                Segment(
                    id = ID.new(),
                    name = "",
                    time = 0.seconds,
                    type = TimeType.STOPWATCH,
                    rank = Rank.from("aaaaaa")
                )
            ),
            rank = Rank.from("aaaaaa")
        )
        val expected = 180.seconds

        val actual = routine.expectedTotalTime

        assertThat(actual).isEqualTo(expected)
    }
}
