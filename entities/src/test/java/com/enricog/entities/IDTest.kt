package com.enricog.entities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IDTest {

    @Test
    fun `test Long to ID`() {
        val expected = ID.from(value = 1L)

        val actual = 1L.asID

        assertEquals(expected, actual)
    }

    @Test
    fun `test Int to ID`() {
        val expected = ID.from(value = 1L)

        val actual = 1.asID

        assertEquals(expected, actual)
    }

    @Test
    fun `test isNew is true when value equal to zero`() {
        val id = 0.asID

        assertTrue(id.isNew)
    }

    @Test
    fun `test isNew is false when value higher than zero`() {
        val id = 1.asID

        assertFalse(id.isNew)
    }

    @Test
    fun `test new should return a new value`() {
        val id = ID.new()

        assertTrue(id.isNew)
    }

    @Test
    fun `test toLong`() {
        val expected = 1L
        val id = 1.asID

        val actual = id.toLong()

        assertEquals(expected, actual)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test initialization with value lower than zero should throw an exception`() {
        ID.from(value = -1L)
    }
}
