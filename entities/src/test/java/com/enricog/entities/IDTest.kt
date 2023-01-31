package com.enricog.entities

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test

class IDTest {

    @Test
    fun `test Long to ID`() {
        val expected = ID.from(value = 1L)

        val actual = 1L.asID

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test Int to ID`() {
        val expected = ID.from(value = 1L)

        val actual = 1.asID

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test isNew is true when value equal to zero`() {
        val id = 0.asID

        assertTrue(id.isNew)
    }

    @Test
    fun `test isNew is false when value higher than zero`() {
        val id = 1.asID

        assertThat(id.isNew).isFalse()
    }

    @Test
    fun `test new should return a new value`() {
        val id = ID.new()

        assertThat(id.isNew).isTrue()
    }

    @Test
    fun `test toLong`() {
        val expected = 1L
        val id = 1.asID

        val actual = id.toLong()

        assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test initialization with value lower than zero should throw an exception`() {
        ID.from(value = -1L)
    }
}
