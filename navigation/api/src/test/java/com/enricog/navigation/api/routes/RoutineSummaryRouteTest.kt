package com.enricog.navigation.api.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.core.entities.asID
import com.enricog.navigation.api.NavigationAction
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutineSummaryRouteTest {

    @Test
    fun `test name`() {
        val expected = "routine/{routineId}/summary"
        val actual = RoutineSummaryRoute.name

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test navigate`() {
        val input = RoutineSummaryRouteInput(routineId = 1.asID)
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(route = "routine/1/summary", navOptions = navOptions)

        val actual = RoutineSummaryRoute.navigate(input = input, optionsBuilder = {})

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test extractInput with routine id`() {
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))
        val expected = RoutineSummaryRouteInput(routineId = 1.asID)

        val actual = RoutineSummaryRoute.extractInput(savedStateHandle = savedStateHandle)

        assertThat(actual).isEqualTo(expected)
    }
}
