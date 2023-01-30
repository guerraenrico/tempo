package com.enricog.features.routines.navigation

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.entities.asID
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.navigation.api.routes.SegmentRoute
import com.enricog.navigation.api.routes.SegmentRouteInput
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRouteInput
import com.enricog.navigation.testing.FakeNavigator
import org.junit.Rule
import org.junit.Test

class RoutinesNavigationActionsTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val navigator = FakeNavigator()

    private val navigationActions = RoutinesNavigationActions(navigator)

    @Test
    fun `test routinesBack`() = coroutineRule {
        navigationActions.goBack()

        navigator.assertGoBack()
    }

    @Test
    fun `test goToRoutineSummary`() = coroutineRule {
        navigationActions.goToRoutineSummary(routineId = 1.asID)

        navigator.assertGoTo(
            route = RoutineSummaryRoute,
            input = RoutineSummaryRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `test goToRoutine`() = coroutineRule {
        navigationActions.goToRoutine(routineId = 1.asID)

        navigator.assertGoTo(route = RoutineRoute, input = RoutineRouteInput(routineId = 1.asID))
    }

    @Test
    fun `test goToSegment`() = coroutineRule {
        navigationActions.goToSegment(routineId = 1.asID, segmentId = 2.asID)

        navigator.assertGoTo(
            route = SegmentRoute,
            input = SegmentRouteInput(
                routineId = 1.asID,
                segmentId = 2.asID
            )
        )
    }

    @Test
    fun `test goToTimer`() = coroutineRule {
        navigationActions.goToTimer(routineId = 1.asID)

        navigator.assertGoTo(route = TimerRoute, input = TimerRouteInput(routineId = 1.asID))
    }
}