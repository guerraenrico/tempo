package com.enricog.features.timer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.enricog.features.timer.settings.TimerSettingsScreen
import com.enricog.navigation.api.extensions.navViewModel
import com.enricog.navigation.api.routes.TimerRoute
import com.enricog.navigation.api.routes.TimerRoute.compose as composeTimer
import com.enricog.navigation.api.routes.TimerSettingsRoute.compose as composeTimerSettings

fun NavGraphBuilder.TimerNavigation() {
    navigation(startDestination = TimerRoute.name, route = "timer") {

        composeTimer { TimerScreen(viewModel = navViewModel(it)) }

        composeTimerSettings { TimerSettingsScreen(viewModel = navViewModel(it)) }
    }
}
