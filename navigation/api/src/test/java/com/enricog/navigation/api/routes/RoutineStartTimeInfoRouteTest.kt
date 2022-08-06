package com.enricog.navigation.api.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.navigation.api.NavigationAction
import org.junit.Test
import kotlin.test.assertEquals

class RoutineStartTimeInfoRouteTest {

    @Test
    fun `test name`() {
        val expected = "routine/start-time-info"

        val actual = RoutineStartTimeInfoRoute.name

        assertEquals(expected, actual)
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

        assertEquals(expected, actual)
    }

    @Test
    fun `test extractInput`() {
        val savedStateHandle = SavedStateHandle(emptyMap())
        val expected = RoutineStartTimeInfoRouteInput

        val actual = RoutineStartTimeInfoRoute.extractInput(savedStateHandle = savedStateHandle)

        assertEquals(expected, actual)
    }


}