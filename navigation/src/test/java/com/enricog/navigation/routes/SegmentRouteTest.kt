package com.enricog.navigation.routes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.navOptions
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
    fun `test navigate with routine id and segment id`() {
        val input = SegmentRouteInput(routineId = 1, segmentId = 2)
        val navOptions = navOptions {}
        val expected = NavigationAction.GoTo(
            route = "routine/1/segment/edit?segmentId=2",
            navOptions = navOptions
        )

        val actual = SegmentRoute.navigate(input = input, optionsBuilder = {})

        assertEquals(expected, actual)
    }

    @Test
    fun `test navigate with routine id and without segment id`() {
        val input = SegmentRouteInput(routineId = 1, segmentId = null)
        val navOptions = navOptions {}
        val expected =
            NavigationAction.GoTo(route = "routine/1/segment/edit", navOptions = navOptions)

        val actual = SegmentRoute.navigate(input = input, optionsBuilder = {})

        assertEquals(expected, actual)
    }

    @Test
    fun `test extractInput with routine id and segment id`() {
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L, "segmentId" to 2L))
        val expected = SegmentRouteInput(routineId = 1, segmentId = 2)

        val actual = SegmentRoute.extractInput(savedStateHandle = savedStateHandle)

        assertEquals(expected, actual)
    }

    @Test
    fun `test extractInput with routine id and without segment id`() {
        val savedStateHandle = SavedStateHandle(mapOf("routineId" to 1L))
        val expected = SegmentRouteInput(routineId = 1, segmentId = 0)

        val actual = SegmentRoute.extractInput(savedStateHandle = savedStateHandle)

        assertEquals(expected, actual)
    }
}
