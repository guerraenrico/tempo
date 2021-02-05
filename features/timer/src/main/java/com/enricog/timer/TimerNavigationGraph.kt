package com.enricog.timer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigation
import com.enricog.timer.navigation.TimerNavigationConstants
import com.enricog.ui_components.ambients.navViewModel

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
            TimerScreen(
                navBackStackEntry.arguments!!.getLong(TimerNavigationConstants.routeIdParamName),
                navViewModel(navBackStackEntry)
            )
        }
    }
}
