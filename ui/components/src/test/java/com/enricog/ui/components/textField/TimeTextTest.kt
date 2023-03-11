package com.enricog.ui.components.textField

import com.enricog.entities.seconds
import com.google.common.truth.Truth.assertThat
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.google.testing.junit.testparameterinjector.TestParameters
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class TimeTextTest {

    @Test
    @TestParameters("{input: \"0 \", expectedSeconds: 0}")
    @TestParameters("{input: \"0\\n\", expectedSeconds: 0}")
    @TestParameters("{input: \"0\\r\", expectedSeconds: 0}")
    @TestParameters("{input: \"0\\r\\n\", expectedSeconds: 0}")
    @TestParameters("{input: \"0\\n0\", expectedSeconds: 0}")
    @TestParameters("{input: \"0000\", expectedSeconds: 0}")
    @TestParameters("{input: \"9\", expectedSeconds: 9}")
    @TestParameters("{input: \"89\", expectedSeconds: 89}")
    @TestParameters("{input: \"891\", expectedSeconds: 571}")
    @TestParameters("{input: \"052\", expectedSeconds: 52}")
    @TestParameters("{input: \"0052\", expectedSeconds: 52}")
    @TestParameters("{input: \"00052\", expectedSeconds: 52}")
    @TestParameters("{input: \"000052\", expectedSeconds: 52}")
    @TestParameters("{input: \"200052\", expectedSeconds: 72052}")
    @TestParameters("{input: \"201052\", expectedSeconds: 72652}")
    @TestParameters("{input: \"2010\\n52\", expectedSeconds: 72652}")
    @TestParameters("{input: \"6000\", expectedSeconds: 3600}")
    @TestParameters("{input: \"60000\", expectedSeconds: 21600}")
    // Over max value
    @TestParameters("{input: \"5000000\", expectedSeconds: 86400}")
    @TestParameters("{input: \"52000000\", expectedSeconds: 86400}")
    fun testToSeconds(input: String, expectedSeconds: Int) {
        val expected = expectedSeconds.seconds

        val actual = input.timeText.toSeconds()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @TestParameters("{input: \"9\", expected: \"00:09\"}")
    @TestParameters("{input: \"50\", expected: \"00:50\"}")
    @TestParameters("{input: \"610\", expected: \"06:10\"}")
    @TestParameters("{input: \"5001\", expected: \"50:01\"}")
    fun toStringFormatted(input: String, expected: String) {
        val actual = input.timeText.toStringFormatted()

        assertThat(actual).isEqualTo(expected)
    }
}