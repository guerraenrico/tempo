package com.enricog.navigation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.enricog.navigation.RouteNavigation

object RoutinesRoute : Route<RoutinesRouteInput> {
    override val name: String = "routines"

    override fun NavGraphBuilder.compose(content: @Composable (NavBackStackEntry) -> Unit) {
        composable(
            route = name,
            arguments = emptyList(),
            content = content
        )
    }

    override fun navigate(
        input: RoutinesRouteInput,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ): RouteNavigation {
        return RouteNavigation(route = "routines", navOptions = null)
    }
}

object RoutinesRouteInput : RouteInput
