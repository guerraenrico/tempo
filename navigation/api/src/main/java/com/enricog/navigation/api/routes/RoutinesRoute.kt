package com.enricog.navigation.api.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.enricog.navigation.api.NavigationAction

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
    ): NavigationAction {
        val options = optionsBuilder?.let { navOptions(it) }
        return NavigationAction.GoTo(route = "routines", navOptions = options)
    }

    override fun extractInput(savedStateHandle: SavedStateHandle): RoutinesRouteInput {
        return RoutinesRouteInput
    }
}

object RoutinesRouteInput : RouteInput
