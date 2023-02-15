package com.enricog.data.routines.api.entities

import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.seconds
import org.junit.Assert.assertThrows
import org.junit.Test

class SegmentTest {

    @Test
    fun `on instantiation should throw exception when time is less than zero`() {
        val time = (-1).seconds

        assertThrows(IllegalArgumentException::class.java) {
            Segment(
                id = 0.asID,
                name = "",
                time = time,
                type = TimeType.TIMER,
                rank = Rank.from("aaaaaa")
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
            rank = Rank.from("aaaaaa")
        )
        assertThrows(IllegalArgumentException::class.java) {
            segment.copy(time = time)
        }
    }

    @Test
    fun `on instantiation should not throw any exception when all argument are valid`() {
        Segment(
            id = 0.asID,
            name = "",
            time = 50.seconds,
            type = TimeType.TIMER,
            rank = Rank.from("aaaaaa")
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
            rank = Rank.from("aaaaaa")
        )
        segment.copy(time = 0.seconds)
    }
}
