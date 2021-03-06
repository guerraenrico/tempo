package com.enricog.timer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navigation
import com.enricog.timer.navigation.TimerNavigationConstants
import com.enricog.ui_components.navigation.navViewModel

fun NavGraphBuilder.TimerNavigation() {
    navigation(startDestination = TimerNavigationConstants.routeName, route = "timer") {
        composable(
            route = TimerNavigationConstants.routeName,
            arguments = listOf(
                navArgument(TimerNavigationConstants.routeIdParamName) {
                    type = NavType.LongType; nullable = false
                }
            )
        ) { navBackStackEntry ->
            TimerScreen(viewModel = navViewModel(navBackStackEntry))
        }
    }
}
