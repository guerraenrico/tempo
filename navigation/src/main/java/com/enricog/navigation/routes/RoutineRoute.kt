package com.enricog.navigation.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navOptions
import com.enricog.navigation.NavigationAction
import com.enricog.navigation.routes.RoutineRoute.Params.routineId

object RoutineRoute : Route<RoutineRouteInput> {
    private object Params {
        const val routineId = "routineId"
    }

    override val name: String = "routine/edit?routineId={$routineId}"

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
        input: RoutineRouteInput,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ): NavigationAction {
        val route = buildString {
            append("routine/edit")
            if (input.routineId != null) {
                append("?routineId=${input.routineId}")
            }
        }
        val options = optionsBuilder?.let { navOptions(it) }
        return NavigationAction.GoTo(route = route, navOptions = options)
    }

    override fun extractInput(savedStateHandle: SavedStateHandle): RoutineRouteInput {
        return RoutineRouteInput(routineId = savedStateHandle.get<Long>(routineId)!!)
    }
}

class RoutineRouteInput(val routineId: Long?) : RouteInput
