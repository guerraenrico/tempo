package com.enricog.routines.navigation

import com.enricog.navigation.NavigationAction
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
import javax.inject.Inject

internal class RoutinesNavigationActionsImpl @Inject constructor(
    private val navigator: Navigator
) : RoutinesNavigationActions {

    override suspend fun routinesBack() {
        navigator.navigate(GoBack)
    }

    override suspend fun goToRoutineSummary(routineId: Long) {
        val routeNavigation = RoutineSummaryRoute.navigate(
            input = RoutineSummaryRouteInput(routineId = routineId),
            optionsBuilder = {
                popUpTo(RoutinesRoute.name) { inclusive = false }
            }
        )
        navigator.navigate(routeNavigation)
    }

    override suspend fun goToRoutine(routineId: Long?) {
        val routeNavigation = RoutineRoute.navigate(
            input = RoutineRouteInput(routineId = routineId),
            optionsBuilder = null
        )
        navigator.navigate(routeNavigation)
    }

    override suspend fun goToSegment(routineId: Long, segmentId: Long?) {
        val routeNavigation = SegmentRoute.navigate(
            input = SegmentRouteInput(routineId = routineId, segmentId = segmentId),
            optionsBuilder = null
        )
        navigator.navigate(routeNavigation)
    }

    override suspend fun goToTimer(routineId: Long) {
        val routeNavigation = TimerRoute.navigate(
            input = TimerRouteInput(routineId = routineId),
            optionsBuilder = {
                popUpTo(RoutinesRoute.name) { inclusive = true }
            }
        )
        navigator.navigate(routeNavigation)
    }
}