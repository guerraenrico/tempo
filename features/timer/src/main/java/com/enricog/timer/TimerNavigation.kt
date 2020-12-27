package com.enricog.timer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigation

fun NavGraphBuilder.TimerNavigation(
    navController: NavHostController
) {
    navigation(startDestination = "timer/{routineId}", route = "timer") {
        composable(
            route = "timer/{routineId}",
            arguments = listOf(navArgument("routineId") {
                type = NavType.LongType; nullable = false
            })
        ) { navBackStackEntry ->
            Timer(navBackStackEntry.arguments!!.getLong("routineId"))
        }
    }
}