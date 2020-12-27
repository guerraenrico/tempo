package com.enricog.routines

import androidx.compose.runtime.Providers
import androidx.compose.ui.platform.AmbientViewModelStoreOwner
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigation
import com.enricog.routines.detail.Routine
import com.enricog.routines.list.Routines

fun NavGraphBuilder.RoutinesNavigation(
    navController: NavHostController
) {
    navigation(startDestination = "routine?routineId={routineId}", route = "routinesNav") {
        composable(
            route = "routines",
            arguments = emptyList(),
            deepLinks = emptyList()
        ) {
            Providers(
                AmbientViewModelStoreOwner provides navController.getViewModelStoreOwner(
                    navController.graph.id
                )
            ) {
                Routines(navController)
            }
        }

        composable(
            route = "routine?routineId={routineId}",
            arguments = listOf(navArgument("routineId") {
                type = NavType.LongType; defaultValue = -1L
            })
        ) { navBackStackEntry ->
            Providers(
                AmbientViewModelStoreOwner provides navController.getViewModelStoreOwner(
                    navController.graph.id
                )
            ) {
                Routine(navController, navBackStackEntry.arguments?.getLong("routineId"))
            }
        }
    }
}