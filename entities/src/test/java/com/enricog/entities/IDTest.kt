package com.enricog.entities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IDTest {

    @Test
    fun `test Long to ID`() {
        val expected = ID.from(value = 1L)

        val actual = 1L.id

        assertEquals(expected, actual)
    }

    @Test
    fun `test Int to ID`() {
        val expected = ID.from(value = 1L)

        val actual = 1.id

        assertEquals(expected, actual)
    }

    @Test
    fun `test isNew is true when value equal to zero`() {
        val id = 0.id

        assertTrue(id.isNew)
    }

    @Test
    fun `test isNew is false when value higher than zero`() {
        val id = 1.id

        assertFalse(id.isNew)
    }

    @Test
    fun `test new should return a new value`() {
        val id = ID.new()

        assertTrue(id.isNew)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test initialization with value lower than zero should throw an exception`() {
        ID.from(value = -1L)
    }
}
