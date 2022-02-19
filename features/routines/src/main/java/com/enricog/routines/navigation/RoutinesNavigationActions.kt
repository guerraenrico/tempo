package com.enricog.routines.navigation

import com.enricog.entities.ID
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
import javax.inject.Inject

internal class RoutinesNavigationActions @Inject constructor(
    private val navigator: Navigator
) {

    suspend fun goBack() {
        navigator.navigate(GoBack)
    }

    suspend fun goToRoutineSummary(routineId: ID) {
        val routeNavigation = RoutineSummaryRoute.navigate(
            input = RoutineSummaryRouteInput(routineId = routineId),
            optionsBuilder = {
                popUpTo(RoutinesRoute.name) { inclusive = false }
            }
        )
        navigator.navigate(routeNavigation)
    }

    suspend fun goToRoutine(routineId: ID) {
        val routeNavigation = RoutineRoute.navigate(
            input = RoutineRouteInput(routineId = routineId),
            optionsBuilder = null
        )
        navigator.navigate(routeNavigation)
    }

    suspend fun goToSegment(routineId: ID, segmentId: ID) {
        val routeNavigation = SegmentRoute.navigate(
            input = SegmentRouteInput(
                routineId = routineId,
                segmentId = segmentId
            ),
            optionsBuilder = null
        )
        navigator.navigate(routeNavigation)
    }

    suspend fun goToTimer(routineId: ID) {
        val routeNavigation = TimerRoute.navigate(
            input = TimerRouteInput(routineId = routineId),
            optionsBuilder = {
                popUpTo(RoutinesRoute.name) { inclusive = true }
            }
        )
        navigator.navigate(routeNavigation)
    }
}