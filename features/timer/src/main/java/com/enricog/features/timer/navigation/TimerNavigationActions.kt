package com.enricog.features.timer.navigation

import com.enricog.navigation.api.Navigator
import com.enricog.navigation.api.routes.RoutinesRoute
import com.enricog.navigation.api.routes.RoutinesRouteInput
import com.enricog.navigation.api.routes.TimerRoute
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