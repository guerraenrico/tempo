package com.enricog.data.routines.api.entities

import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import org.junit.Assert.assertThrows
import org.junit.Test

class SegmentTest {

    @Test
    fun `on init should throw exception when time is less than zero`() {
        val time = (-1).seconds

        assertThrows(IllegalArgumentException::class.java) {
            Segment(
                id = 0.asID,
                name = "",
                time = time,
                type = TimeType.TIMER,
                rank = Rank.from("aaaaaa"),
                rounds = 1
            )
        }
    }

    @Test
    @Suppress("UnusedDataClassCopyResult")
    fun `on copy should throw exception when time is less that zero`() {
        val time = (-1).seconds

        val segment = Segment(
            id = 0.asID,
            name = "",
            time = 50.seconds,
            type = TimeType.TIMER,
            rank = Rank.from("aaaaaa"),
            rounds = 1
        )
        assertThrows(IllegalArgumentException::class.java) {
            segment.copy(time = time)
        }
    }

    @Test
    fun `on init should throw exception when number of rounds is less than one`() {
        val rounds = 0

        assertThrows(IllegalArgumentException::class.java) {
            Segment(
                id = 0.asID,
                name = "",
                time = 10.seconds,
                type = TimeType.TIMER,
                rank = Rank.from("aaaaaa"),
                rounds = rounds
            )
        }
    }

    @Test
    @Suppress("UnusedDataClassCopyResult")
    fun `on copy should throw exception when number of rounds is less that one`() {
        val rounds = -1

        val segment = Segment(
            id = 0.asID,
            name = "",
            time = 50.seconds,
            type = TimeType.TIMER,
            rank = Rank.from("aaaaaa"),
            rounds = 1
        )
        assertThrows(IllegalArgumentException::class.java) {
            segment.copy(rounds = rounds)
        }
    }

    @Test
    fun `on init should not throw any exception when all argument are valid`() {
        Segment(
            id = 0.asID,
            name = "",
            time = 50.seconds,
            type = TimeType.TIMER,
            rank = Rank.from("aaaaaa"),
            rounds = 1
        )
    }

    @Test
    @Suppress("UnusedDataClassCopyResult")
    fun `on copy should not throw any exception when all argument are valid`() {
        val segment = Segment(
            id = 0.asID,
            name = "",
            time = 50.seconds,
            type = TimeType.TIMER,
            rank = Rank.from("aaaaaa"),
            rounds = 1
        )
        segment.copy(time = 0.seconds)
    }
}
