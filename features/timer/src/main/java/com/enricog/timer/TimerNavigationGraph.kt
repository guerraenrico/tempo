package com.enricog.timer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.enricog.navigation.extensions.navViewModel
import com.enricog.navigation.routes.TimerRoute.compose
import com.enricog.timer.navigation.TimerNavigationConstants

fun NavGraphBuilder.TimerNavigation() {
    navigation(startDestination = TimerNavigationConstants.routeName, route = "timer") {
        compose { navBackStackEntry ->
            TimerScreen(viewModel = navViewModel(navBackStackEntry))
        }
    }
}
