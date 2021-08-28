package com.enricog.navigation.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.navigation.NavigationAction
import kotlin.test.assertEquals
import org.junit.Test

class TimerRouteTest {

    @Test
    fun `test name`() {
        val expected = "timer/{routineId}"
        val actual = TimerRoute.name

        assertEquals(expected, actual)
    }

    @Test
    fun `test navigate with routine id`() {
        val input = TimerRouteInput(routineId = 1)
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(route = "timer/1", navOptions = navOptions)

        val actual = TimerRoute.navigate(input = input, optionsBuilder = {})

        assertEquals(expected, actual)
    }

    @Test
    fun `test extractInput with routine id`() {
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))
        val expected = TimerRouteInput(routineId = 1)

        val actual = TimerRoute.extractInput(savedStateHandle = savedStateHandle)

        assertEquals(expected, actual)
    }
}
