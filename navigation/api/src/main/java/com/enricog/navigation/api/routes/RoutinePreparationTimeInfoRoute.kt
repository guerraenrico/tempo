package com.enricog.navigation.api.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.enricog.navigation.api.NavigationAction
import com.enricog.navigation.api.extensions.bottomSheet

object RoutinePreparationTimeInfoRoute : Route<RoutinePreparationTimeInfoRouteInput> {

    override val name: String = "routine/preparation-time-info"

    override fun NavGraphBuilder.compose(content: @Composable (NavBackStackEntry) -> Unit) {
        bottomSheet(
            route = name,
            content = { content(it) },
        )
    }

    override fun navigate(
        input: RoutinePreparationTimeInfoRouteInput,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ): NavigationAction {
        val options = optionsBuilder?.let { navOptions(it) }
        return NavigationAction.GoTo(route = name, navOptions = options)
    }

    override fun extractInput(savedStateHandle: SavedStateHandle): RoutinePreparationTimeInfoRouteInput {
        return RoutinePreparationTimeInfoRouteInput
    }
}

object RoutinePreparationTimeInfoRouteInput : RouteInput