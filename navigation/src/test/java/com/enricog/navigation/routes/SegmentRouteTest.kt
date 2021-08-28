package com.enricog.navigation.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
import com.enricog.entities.asID
import com.enricog.navigation.NavigationAction
import kotlin.test.assertEquals
import org.junit.Test

class SegmentRouteTest {

    @Test
    fun `test name`() {
        val expected = "routine/{routineId}/segment/edit?segmentId={segmentId}"
        val actual = SegmentRoute.name

        assertEquals(expected, actual)
    }

    @Test
    fun `test navigate`() {
        val input = SegmentRouteInput(routineId = 1.asID, segmentId = 2.asID)
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(
            route = "routine/1/segment/edit?segmentId=2",
            navOptions = navOptions
        )

        val actual = SegmentRoute.navigate(input = input, optionsBuilder = {})

        assertEquals(expected, actual)
    }

    @Test
    fun `test extractInput`() {
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L, "segmentId" to 2L))
        val expected = SegmentRouteInput(routineId = 1.asID, segmentId = 2.asID)

        val actual = SegmentRoute.extractInput(savedStateHandle = savedStateHandle)

        assertEquals(expected, actual)
    }
}
