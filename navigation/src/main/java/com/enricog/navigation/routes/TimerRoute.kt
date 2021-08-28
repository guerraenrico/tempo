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
import com.enricog.entities.ID
import com.enricog.navigation.NavigationAction
import com.enricog.navigation.routes.TimerRoute.Params.routineId

object TimerRoute : Route<TimerRouteInput> {
    private object Params {
        const val routineId = "routineId"
    }

    override val name: String = "timer/{$routineId}"

    override fun NavGraphBuilder.compose(content: @Composable (NavBackStackEntry) -> Unit) {
        composable(
            route = name,
            arguments = listOf(
                navArgument(routineId) {
                    type = NavType.LongType; nullable = false
                }
            ),
            content = content
        )
    }

    override fun navigate(
        input: TimerRouteInput,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ): NavigationAction {
        val route = "timer/${input.routineId.toLong()}"
        val options = optionsBuilder?.let { navOptions(it) }
        return NavigationAction.GoTo(route = route, navOptions = options)
    }

    override fun extractInput(savedStateHandle: SavedStateHandle): TimerRouteInput {
        val id = ID.from(savedStateHandle.get<Long>(routineId)!!)
        return TimerRouteInput(routineId = id)
    }
}

data class TimerRouteInput(val routineId: ID) : RouteInput
