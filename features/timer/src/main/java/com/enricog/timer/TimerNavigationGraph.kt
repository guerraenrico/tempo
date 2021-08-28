package com.enricog.timer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.enricog.navigation.extensions.navViewModel
import com.enricog.navigation.routes.TimerRoute
import com.enricog.navigation.routes.TimerRoute.compose

fun NavGraphBuilder.TimerNavigation() {
    navigation(startDestination = TimerRoute.name, route = "timer") {
        compose { TimerScreen(viewModel = navViewModel(it)) }
    }
}
