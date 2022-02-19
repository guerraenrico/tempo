package com.enricog.navigation.api.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.entities.asID
import com.enricog.navigation.api.NavigationAction
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
    fun `test navigate`() {
        val input = TimerRouteInput(routineId = 1.asID)
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(route = "timer/1", navOptions = navOptions)

        val actual = TimerRoute.navigate(input = input, optionsBuilder = {})

        assertEquals(expected, actual)
    }

    @Test
    fun `test extractInput`() {
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))
        val expected = TimerRouteInput(routineId = 1.asID)

        val actual = TimerRoute.extractInput(savedStateHandle = savedStateHandle)

        assertEquals(expected, actual)
    }
}
