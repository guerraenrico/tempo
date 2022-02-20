package com.enricog.navigation.api.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.navigation.api.NavigationAction
import kotlin.test.assertEquals
import org.junit.Test

class RoutinesRouteTest {

    @Test
    fun `test name`() {
        val expected = "routines"

        val actual = RoutinesRoute.name

        assertEquals(expected, actual)
    }

    @Test
    fun `test navigate`() {
        val input = RoutinesRouteInput
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(route = "routines", navOptions = navOptions)

        val actual = RoutinesRoute.navigate(input = input, optionsBuilder = {})

        assertEquals(expected, actual)
    }

    @Test
    fun `test extractInput`() {
        val savedStateHandle = SavedStateHandle(emptyMap())
        val expected = RoutinesRouteInput

        val actual = RoutinesRoute.extractInput(savedStateHandle = savedStateHandle)

        assertEquals(expected, actual)
    }
}
