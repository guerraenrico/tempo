package com.enricog.ui.components.textField

import com.enricog.entities.seconds
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class TimeTextTest {

    @ParameterizedTest
    @ArgumentsSource(ValidArgumentProvider::class)
    fun `test to seconds`(input: String, expectedSeconds: Int) {
        val expected = expectedSeconds.seconds
        val actual = input.timeText.toSeconds()
        assertEquals(expected, actual)
    }

    private class ValidArgumentProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                "9" to 9,
                "89" to 89,
                "891" to 571,
                "052" to 52,
                "0052" to 52,
                "00052" to 52,
                "000052" to 52,
                "200052" to 72052,
                "201052" to 72652,
                "6000" to 3600,
                "60000" to 21600,
                // Over max value
                "5000000" to 86400,
                "52000000" to 86400,
            ).map { (t, s) -> Arguments.of(t, s) }
        }
    }
}