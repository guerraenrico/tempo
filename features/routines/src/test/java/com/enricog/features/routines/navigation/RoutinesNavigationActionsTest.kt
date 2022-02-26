package com.enricog.features.routines.navigation

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.entities.asID
import com.enricog.navigation.api.NavigationAction.GoBack
import com.enricog.navigation.api.Navigator
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.navigation.api.routes.RoutinesRoute
import com.enricog.navigation.api.routes.SegmentRoute
import com.enricog.navigation.api.routes.SegmentRouteInput
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
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
            input = RoutineSummaryRouteInput(routineId = 1.asID),
            optionsBuilder = {
                popUpTo(RoutinesRoute.name) { inclusive = false }
            }
        )

        sut.goToRoutineSummary(routineId = 1.asID)

        coVerify { navigator.navigate(expected) }
    }

    @Test
    fun `test goToRoutine`() = coroutineRule {
        val expected = RoutineRoute.navigate(
            input = RoutineRouteInput(routineId = 1.asID),
            optionsBuilder = null
        )

        sut.goToRoutine(routineId = 1.asID)

        coVerify { navigator.navigate(expected) }
    }

    @Test
    fun `test goToSegment`() = coroutineRule {
        val expected = SegmentRoute.navigate(
            input = SegmentRouteInput(
                routineId = 1.asID,
                segmentId = 2.asID
            ),
            optionsBuilder = null
        )

        sut.goToSegment(routineId = 1.asID, segmentId = 2.asID)

        coVerify { navigator.navigate(expected) }
    }

    @Test
    fun `test goToTimer`() = coroutineRule {
        val expected = TimerRoute.navigate(
            input = TimerRouteInput(routineId = 1.asID),
            optionsBuilder = {
                popUpTo(RoutinesRoute.name) { inclusive = true }
            }
        )

        sut.goToTimer(routineId = 1.asID)

        coVerify { navigator.navigate(expected) }
    }
}