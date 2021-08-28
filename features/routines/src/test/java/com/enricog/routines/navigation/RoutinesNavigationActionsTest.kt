package com.enricog.routines.navigation

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.navigation.NavigationAction.GoBack
import com.enricog.navigation.Navigator
import com.enricog.navigation.routes.RoutineRoute
import com.enricog.navigation.routes.RoutineRouteInput
import com.enricog.navigation.routes.RoutineSummaryRoute
import com.enricog.navigation.routes.RoutineSummaryRouteInput
import com.enricog.navigation.routes.RoutinesRoute
import com.enricog.navigation.routes.SegmentRoute
import com.enricog.navigation.routes.SegmentRouteInput
import com.enricog.navigation.routes.TimerRoute
import com.enricog.navigation.routes.TimerRouteInput
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class RoutinesNavigationActionsTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val navigator: Navigator = mockk(relaxUnitFun = true)

    private val sut = RoutinesNavigationActions(navigator)

    @Test
    fun `test routinesBack`() = coroutineRule {
        val expected = GoBack

        sut.goBack()

        coVerify { navigator.navigate(expected) }
    }

    @Test
    fun `test goToRoutineSummary`() = coroutineRule {
        val expected = RoutineSummaryRoute.navigate(
            input = RoutineSummaryRouteInput(routineId = 1),
            optionsBuilder = {
                popUpTo(RoutinesRoute.name) { inclusive = false }
            }
        )

        sut.goToRoutineSummary(routineId = 1)

        coVerify { navigator.navigate(expected) }
    }

    @Test
    fun `test goToRoutine`() = coroutineRule {
        val expected = RoutineRoute.navigate(
            input = RoutineRouteInput(routineId = 1),
            optionsBuilder = null
        )

        sut.goToRoutine(routineId = 1)

        coVerify { navigator.navigate(expected) }
    }

    @Test
    fun `test goToSegment`() = coroutineRule {
        val expected = SegmentRoute.navigate(
            input = SegmentRouteInput(routineId = 1, segmentId = 2),
            optionsBuilder = null
        )

        sut.goToSegment(routineId = 1, segmentId = 2)

        coVerify { navigator.navigate(expected) }
    }

    @Test
    fun `test goToTimer`() = coroutineRule {
        val expected = TimerRoute.navigate(
            input = TimerRouteInput(routineId = 1),
            optionsBuilder = {
                popUpTo(RoutinesRoute.name) { inclusive = true }
            }
        )

        sut.goToTimer(routineId = 1)

        coVerify { navigator.navigate(expected) }
    }
}