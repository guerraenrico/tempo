package com.enricog.routines

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigation
import com.enricog.routines.detail.Routine
import com.enricog.routines.list.Routines
import com.enricog.routines.navigation.RoutinesNavigationConstants

fun NavGraphBuilder.RoutinesNavigation() {
    navigation(
        startDestination = RoutinesNavigationConstants.Routines.routeName,
        route = "routinesNav"
    ) {
        composable(
            route = RoutinesNavigationConstants.Routines.routeName,
            arguments = emptyList()
        ) {
            Routines()
        }

        composable(
            route = RoutinesNavigationConstants.Routine.routeName,
            arguments = listOf(navArgument(RoutinesNavigationConstants.Routine.routeIdParamName) {
                type = NavType.LongType; defaultValue = -1L
            })
        ) { navBackStackEntry ->
            Routine(navBackStackEntry.arguments?.getLong(RoutinesNavigationConstants.Routine.routeIdParamName))
        }
    }
}
