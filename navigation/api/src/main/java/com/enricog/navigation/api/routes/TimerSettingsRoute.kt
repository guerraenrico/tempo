package com.enricog.navigation.api.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.enricog.navigation.api.NavigationAction
import com.enricog.navigation.api.extensions.bottomSheet

object TimerSettingsRoute : Route<TimerSettingsRoute.Input> {

    override val name: String = "timer/settings"

    override fun NavGraphBuilder.compose(content: @Composable (NavBackStackEntry) -> Unit) {
        bottomSheet(
            route = name,
            content = { content(it) },
        )
    }

    override fun navigate(
        input: Input,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ): NavigationAction {
        val options = optionsBuilder?.let { navOptions(it) }
        return NavigationAction.GoTo(route = name, navOptions = options)
    }

    override fun extractInput(savedStateHandle: SavedStateHandle): Input {
        return Input
    }

    object Input : RouteInput
}
