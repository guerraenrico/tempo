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
import com.enricog.navigation.api.routes.TimerRoute.Params.routineId

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
                    type = NavType.LongType
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
