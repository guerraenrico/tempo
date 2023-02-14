package com.enricog.navigation.api.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.navigation.api.NavigationAction
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutinePreparationTimeInfoRouteTest {

    @Test
    fun `test name`() {
        val expected = "routine/preparation-time-info"

        val actual = RoutinePreparationTimeInfoRoute.name

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test navigate`() {
        val input = RoutinePreparationTimeInfoRouteInput
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(
            route = "routine/preparation-time-info",
            navOptions = navOptions
        )

        val actual = RoutinePreparationTimeInfoRoute.navigate(input = input, optionsBuilder = {})

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test extractInput`() {
        val savedStateHandle = SavedStateHandle(emptyMap())
        val expected = RoutinePreparationTimeInfoRouteInput

        val actual = RoutinePreparationTimeInfoRoute.extractInput(savedStateHandle = savedStateHandle)

        assertThat(actual).isEqualTo(expected)
    }
}
