package com.enricog.timer.navigation

import com.enricog.navigation.Navigator
import com.enricog.navigation.routes.RoutinesRoute
import com.enricog.navigation.routes.RoutinesRouteInput
import com.enricog.navigation.routes.TimerRoute
import javax.inject.Inject

internal class TimerNavigationActions @Inject constructor(
    private val navigator: Navigator
) {

    suspend fun backToRoutines() {
        val routeNavigation = RoutinesRoute.navigate(RoutinesRouteInput) {
            popUpTo(TimerRoute.name) { inclusive = true }
        }
        navigator.navigate(routeNavigation)
    }
}