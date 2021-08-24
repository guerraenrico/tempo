package com.enricog.navigation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navOptions
import com.enricog.navigation.NavigationAction
import com.enricog.navigation.routes.RoutineSummaryRoute.Params.routineId

object RoutineSummaryRoute : Route<RoutineSummaryRouteInput> {
    private object Params {
        const val routineId = "routineId"
    }

    override val name: String = "routine/{$routineId}/summary"

    override fun NavGraphBuilder.compose(content: @Composable (NavBackStackEntry) -> Unit) {
        composable(
            route = name,
            arguments = listOf(
                navArgument(routineId) {
                    type = NavType.LongType; defaultValue = 0L
                }
            ),
            content = content
        )
    }

    override fun navigate(
        input: RoutineSummaryRouteInput,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ): NavigationAction {
        val route = "routine/${input.routineId}/summary"
        val options = optionsBuilder?.let { navOptions(it) }
        return NavigationAction.GoTo(route = route, navOptions = options)
    }
}

class RoutineSummaryRouteInput(val routineId: Long) : RouteInput
