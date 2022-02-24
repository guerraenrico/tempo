package com.enricog.features.timer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.enricog.navigation.api.extensions.navViewModel
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRoute.compose

fun NavGraphBuilder.TimerNavigation() {
    navigation(startDestination = TimerRoute.name, route = "timer") {
        compose { TimerScreen(viewModel = navViewModel(it)) }
    }
}
