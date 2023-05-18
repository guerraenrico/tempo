package com.enricog.libraries.notification.api.notification

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.enricog.navigation.api.NavigationAction
import com.enricog.navigation.api.routes.Route
import com.enricog.navigation.api.routes.RouteInput
import com.google.accompanist.navigation.material.bottomSheet

internal object NotificationPermissionRoute : Route<NotificationPermissionRoute.Input> {

    override val name: String = "permissions/notification"

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
