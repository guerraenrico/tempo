package com.enricog.navigation.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.navigation.NavigationAction
import kotlin.test.assertEquals
import org.junit.Test

class RoutineRouteTest {

    @Test
    fun `test name`() {
        val expected = "routine/edit?routineId={routineId}"
        val actual = RoutineRoute.name

        assertEquals(expected, actual)
    }

    @Test
    fun `test navigate with routine id`() {
        val input = RoutineRouteInput(routineId = 1)
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(route = "routine/edit?routineId=1", navOptions = navOptions)

        val actual = RoutineRoute.navigate(input = input, optionsBuilder = {})

        assertEquals(expected, actual)
    }

    @Test
    fun `test navigate without routine id`() {
        val input = RoutineRouteInput(routineId = null)
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(route = "routine/edit", navOptions = navOptions)

        val actual = RoutineRoute.navigate(input = input, optionsBuilder = {})

        assertEquals(expected, actual)
    }

    @Test
    fun `test extractInput with routine id`() {
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))
        val expected = RoutineRouteInput(routineId = 1)

        val actual = RoutineRoute.extractInput(savedStateHandle = savedStateHandle)

        assertEquals(expected, actual)
    }

    @Test
    fun `test extractInput without routine id`() {
        val savedStateHandle = SavedStateHandle(emptyMap())
        val expected = RoutineRouteInput(routineId = 0)

        val actual = RoutineRoute.extractInput(savedStateHandle = savedStateHandle)

        assertEquals(expected, actual)
    }
}
