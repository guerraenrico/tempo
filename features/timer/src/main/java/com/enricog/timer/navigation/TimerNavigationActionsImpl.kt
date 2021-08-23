package com.enricog.timer.navigation

import com.enricog.navigation.Navigator
import com.enricog.navigation.routes.RoutinesRoute
import com.enricog.navigation.routes.RoutinesRouteInput
import com.enricog.navigation.routes.TimerRoute
import javax.inject.Inject

internal class TimerNavigationActionsImpl @Inject constructor(
    private val navigator: Navigator
) : TimerNavigationActions {

    override fun backToRoutines() {
        val routeNavigation = RoutinesRoute.navigate(RoutinesRouteInput) {
            popUpTo(TimerRoute.name) { inclusive = true }
        }
        navigator.goTo(routeNavigation)
    }
}