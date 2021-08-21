package com.enricog.entities.routines

import com.enricog.entities.seconds
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class SegmentTest {
    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    @Test
    fun `on instantiation should throw exception when time is less than zero`() {
        val time = (-1).seconds

        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("time must be positive")

        Segment(
            id = 0,
            name = "",
            time = time,
            type = TimeType.TIMER
        )
    }

    @Suppress("UnusedDataClassCopyResult")
    @Test
    fun `on copy should throw exception when time is less that zero`() {
        val time = (-1).seconds

        exceptionRule.expect(IllegalArgumentException::class.java)
        exceptionRule.expectMessage("time must be positive")

        val segment = Segment(
            id = 0,
            name = "",
            time = 50.seconds,
            type = TimeType.TIMER
        )
        segment.copy(time = time)
    }

    @Test
    fun `on instantiation should not throw any exception when all argument are valid`() {
        Segment(
            id = 0,
            name = "",
            time = 50.seconds,
            type = TimeType.TIMER
        )
    }

    @Suppress("UnusedDataClassCopyResult")
    @Test
    fun `on copy should not throw any exception when all argument are valid`() {
        val segment = Segment(
            id = 0,
            name = "",
            time = 50.seconds,
            type = TimeType.TIMER
        )
        segment.copy(time = 0.seconds)
    }
}
