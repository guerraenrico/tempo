package com.enricog.navigation.api.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.core.entities.asID
import com.enricog.navigation.api.NavigationAction
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutineRouteTest {

    @Test
    fun `test name`() {
        val expected = "routine/edit?routineId={routineId}"
        val actual = RoutineRoute.name

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test navigate`() {
        val input = RoutineRouteInput(routineId = 1.asID)
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(
            route = "routine/edit?routineId=1",
            navOptions = navOptions
        )

        val actual = RoutineRoute.navigate(input = input, optionsBuilder = {})

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test extractInput`() {
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))
        val expected = RoutineRouteInput(routineId = 1.asID)

        val actual = RoutineRoute.extractInput(savedStateHandle = savedStateHandle)

        assertThat(actual).isEqualTo(expected)
    }
}
