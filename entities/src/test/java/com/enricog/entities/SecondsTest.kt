package com.enricog.entities

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SecondsTest {

    @Test
    fun `test Int to seconds`() {
        val value = 120
        val expected = 120L

        val actual = value.seconds

        assertThat(actual.value).isEqualTo(expected)
    }

    @Test
    fun `test Long to seconds`() {
        val value = 120L
        val expected = 120L

        val actual = value.seconds

        assertThat(actual.value).isEqualTo(expected)
    }

    @Test
    fun `test String to seconds when blank string`() {
        val value = ""
        val expected = 0L

        val actual = value.seconds

        assertThat(actual.value).isEqualTo(expected)
    }

    @Test
    fun `test String to seconds when represent only seconds`() {
        val value = "12"
        val expected = 12L

        val actual = value.seconds

        assertThat(actual.value).isEqualTo(expected)
    }

    @Test
    fun `test String to seconds when represent minutes as zero and seconds`() {
        val value = "0023"
        val expected = 23L

        val actual = value.seconds

        assertThat(actual.value).isEqualTo(expected)
    }

    @Test
    fun `test String to seconds when represent minutes and seconds`() {
        val value = "123"
        val expected = 123L

        val actual = value.seconds

        assertThat(actual.value).isEqualTo(expected)
    }

    @Test
    fun `test String to seconds when represent minutes (double digit with 0) and seconds`() {
        val value = "0123"
        val expected = 123L

        val actual = value.seconds

        assertThat(actual.value).isEqualTo(expected)
    }

    @Test
    fun `test String to seconds when represent minutes (double digit) and seconds`() {
        val value = "1234"
        val expected = 1234L

        val actual = value.seconds

        assertThat(actual.value).isEqualTo(expected)
    }

    @Test
    fun `test String to seconds when represent hours, minutes and seconds`() {
        val value = "012301"
        val expected = 12301L

        val actual = value.seconds

        assertThat(actual.value).isEqualTo(expected)
    }

    @Test
    fun `test toString minutes`() {
        val value = 130.seconds
        val expected = "130"

        val actual = value.toString()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test toString seconds`() {
        val value = 45.seconds
        val expected = "45"

        val actual = value.toString()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test toString minutes double digit`() {
        val value = 500.seconds
        val expected = "500"

        val actual = value.toString()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test plus`() {
        val value1 = 120.seconds
        val value2 = 50.seconds
        val expected = 170.seconds

        val actual = value1 + value2

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test minus`() {
        val value1 = 120.seconds
        val value2 = 50.seconds
        val expected = 70.seconds

        val actual = value1 - value2

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test minutes`() {
        val value = 500.seconds
        val expected = 8L to 20L

        val actual = value.inMinutes

        assertThat(actual).isEqualTo(expected)
    }
}
