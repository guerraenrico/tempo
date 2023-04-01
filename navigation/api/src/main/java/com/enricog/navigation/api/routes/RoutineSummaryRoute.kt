package com.enricog.navigation.api.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import com.enricog.core.entities.ID
import com.enricog.navigation.api.NavigationAction
import com.enricog.navigation.api.routes.RoutineSummaryRoute.Params.routineId

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
                    type = NavType.LongType
                }
            ),
            content = content
        )
    }

    override fun navigate(
        input: RoutineSummaryRouteInput,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ): NavigationAction {
        val route = "routine/${input.routineId.toLong()}/summary"
        val options = optionsBuilder?.let { navOptions(it) }
        return NavigationAction.GoTo(route = route, navOptions = options)
    }

    override fun extractInput(savedStateHandle: SavedStateHandle): RoutineSummaryRouteInput {
        val id = ID.from(savedStateHandle.get<Long>(routineId)!!)
        return RoutineSummaryRouteInput(routineId = id)
    }
}

data class RoutineSummaryRouteInput(val routineId: ID) : RouteInput
