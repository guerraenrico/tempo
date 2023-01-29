package com.enricog.navigation.api.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.entities.asID
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SegmentRouteTest {

    @Test
    fun `test name`() {
        val expected = "routine/{routineId}/segment/edit?segmentId={segmentId}"
        val actual = SegmentRoute.name

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test navigate`() {
        val input = SegmentRouteInput(
            routineId = 1.asID,
            segmentId = 2.asID
        )
        val navOptions = navOptions {}
        val expected = com.enricog.navigation.api.NavigationAction.GoTo(
            route = "routine/1/segment/edit?segmentId=2",
            navOptions = navOptions
        )

        val actual = SegmentRoute.navigate(input = input, optionsBuilder = {})

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test extractInput`() {
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L, "segmentId" to 2L))
        val expected = SegmentRouteInput(
            routineId = 1.asID,
            segmentId = 2.asID
        )

        val actual = SegmentRoute.extractInput(savedStateHandle = savedStateHandle)

        assertThat(actual).isEqualTo(expected)
    }
}
