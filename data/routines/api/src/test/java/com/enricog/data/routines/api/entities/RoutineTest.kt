package com.enricog.data.routines.api.entities

import com.enricog.data.routines.api.entities.Routine.Companion.MAX_PREPARATION_TIME
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
    fun `on instantiation should throw exception when preparation time is more than max`() {
        val preparationTime = MAX_PREPARATION_TIME + 1.seconds

        assertThrows(IllegalArgumentException::class.java) {
            Routine(
                id = 0.asID,
                name = "",
                preparationTime = preparationTime,
                createdAt = OffsetDateTime.MAX,
                updatedAt = OffsetDateTime.MAX,
                segments = emptyList(),
                rank = Rank.from("aaaaaa")
            )
        }
    }

    @Test
    fun `on init should throw exception when preparation time is less than zero`() {
        val preparationTime = (-1).seconds

        assertThrows(IllegalArgumentException::class.java) {
            Routine(
                id = 0.asID,
                name = "",
                preparationTime = preparationTime,
                createdAt = OffsetDateTime.MAX,
                updatedAt = OffsetDateTime.MAX,
                segments = emptyList(),
                rank = Rank.from("aaaaaa")
            )
        }
    }

    @Test
    @Suppress("UnusedDataClassCopyResult")
    fun `on copy should throw exception when preparation time is more than max`() {
        val preparationTime = MAX_PREPARATION_TIME + 1.seconds

        val routine = Routine(
            id = 0.asID,
            name = "",
            preparationTime = 50.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )

        assertThrows(IllegalArgumentException::class.java) {
            routine.copy(preparationTime = preparationTime)
        }
    }

    @Test
    @Suppress("UnusedDataClassCopyResult")
    fun `on copy should throw exception when preparation time is less that zero`() {
        val preparationTime = (-1).seconds

        val routine = Routine(
            id = 0.asID,
            name = "",
            preparationTime = 50.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )
        assertThrows(IllegalArgumentException::class.java) {
            routine.copy(preparationTime = preparationTime)
        }
    }

    @Test
    fun `on instantiation should not throw any exception when all argument are valid`() {
        Routine(
            id = 0.asID,
            name = "",
            preparationTime = MAX_PREPARATION_TIME,
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
            preparationTime = 50.seconds,
            createdAt = OffsetDateTime.MAX,
            updatedAt = OffsetDateTime.MAX,
            segments = emptyList(),
            rank = Rank.from("aaaaaa")
        )
        routine.copy(preparationTime = MAX_PREPARATION_TIME)
    }

    @Test
    fun `expected total time should be zero when routine has only stopwatch segment`() {
        val routine = Routine(
            id = 0.asID,
            name = "",
            preparationTime = 30.seconds,
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
            preparationTime = 30.seconds,
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
