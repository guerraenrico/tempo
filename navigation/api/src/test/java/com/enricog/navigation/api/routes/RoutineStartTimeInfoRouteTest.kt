package com.enricog.navigation.api.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.navigation.api.NavigationAction
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutineStartTimeInfoRouteTest {

    @Test
    fun `test name`() {
        val expected = "routine/start-time-info"

        val actual = RoutineStartTimeInfoRoute.name

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test navigate`() {
        val input = RoutineStartTimeInfoRouteInput
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(
            route = "routine/start-time-info",
            navOptions = navOptions
        )

        val actual = RoutineStartTimeInfoRoute.navigate(input = input, optionsBuilder = {})

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test extractInput`() {
        val savedStateHandle = SavedStateHandle(emptyMap())
        val expected = RoutineStartTimeInfoRouteInput

        val actual = RoutineStartTimeInfoRoute.extractInput(savedStateHandle = savedStateHandle)

        assertThat(actual).isEqualTo(expected)
    }
}
