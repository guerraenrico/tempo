package com.enricog.entities

import org.junit.Assert.assertEquals
import org.junit.Test

class SecondsTest {

    @Test
    fun `test Int to seconds`() {
        val value = 120
        val expected = 120L

        val actual = value.seconds

        assertEquals(expected, actual.value)
    }

    @Test
    fun `test Long to seconds`() {
        val value = 120L

        val actual = value.seconds

        assertEquals(value, actual.value)
    }

    @Test
    fun `test String to seconds when blank string`() {
        val value = ""
        val expected = 0L

        val actual = value.seconds

        assertEquals(expected, actual.value)
    }

    @Test
    fun `test String to seconds when represent only seconds`() {
        val value = "12"
        val expected = 12L

        val actual = value.seconds

        assertEquals(expected, actual.value)
    }

    @Test
    fun `test String to seconds when represent minutes as zero and seconds`() {
        val value = "00:23"
        val expected = 23L

        val actual = value.seconds

        assertEquals(expected, actual.value)
    }

    @Test
    fun `test String to seconds when represent minutes and seconds`() {
        val value = "1:23"
        val expected = 83L

        val actual = value.seconds

        assertEquals(expected, actual.value)
    }

    @Test
    fun `test String to seconds when represent minutes (double digit with 0) and seconds`() {
        val value = "01:23"
        val expected = 83L

        val actual = value.seconds

        assertEquals(expected, actual.value)
    }

    @Test
    fun `test String to seconds when represent minutes (double digit) and seconds`() {
        val value = "12:34"
        val expected = 754L

        val actual = value.seconds

        assertEquals(expected, actual.value)
    }

    @Test
    fun `test String to seconds when represent hours, minutes and seconds`() {
        val value = "01:23:01"
        val expected = 4981L

        val actual = value.seconds

        assertEquals(expected, actual.value)
    }

    @Test
    fun `test toString minutes`() {
        val value = 130.seconds
        val expected = "2:10"

        val actual = value.toString()

        assertEquals(expected, actual)
    }

    @Test
    fun `test toString seconds`() {
        val value = 45.seconds
        val expected = "0:45"

        val actual = value.toString()

        assertEquals(expected, actual)
    }

    @Test
    fun `test toString minutes double digit`() {
        val value = 500.seconds
        val expected = "8:20"

        val actual = value.toString()

        assertEquals(expected, actual)
    }

    @Test
    fun `test plus`() {
        val value1 = 120.seconds
        val value2 = 50.seconds
        val expected = 170.seconds

        val actual = value1 + value2

        assertEquals(expected, actual)
    }

    @Test
    fun `test minus`() {
        val value1 = 120.seconds
        val value2 = 50.seconds
        val expected = 70.seconds

        val actual = value1 - value2

        assertEquals(expected, actual)
    }

    @Test
    fun `test minutes`() {
        val value = 500.seconds
        val expected = 8L to 20L

        val actual = value.inMinutes
        assertEquals(expected, actual)
    }
}
