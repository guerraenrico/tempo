package com.enricog.features.timer.navigation

import com.enricog.navigation.api.Navigator
import com.enricog.navigation.api.routes.RoutinesRoute
import com.enricog.navigation.api.routes.RoutinesRouteInput
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerSettingsRoute
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

    suspend fun openTimerSettings() {
        val routeNavigation = TimerSettingsRoute.navigate(input = TimerSettingsRoute.Input, optionsBuilder = null)
        navigator.navigate(routeNavigation)
    }
}