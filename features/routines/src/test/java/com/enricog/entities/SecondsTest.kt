package com.enricog.entities

import org.junit.Assert.assertEquals
import org.junit.Test

class SecondsTest {

    @Test
    fun `test Int to seconds`() {
        val value = 120

        val actual = value.seconds

        assertEquals(value.toLong(), actual.value)
    }

    @Test
    fun `test Long to seconds`() {
        val value = 120L

        val actual = value.seconds

        assertEquals(value, actual.value)
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

}