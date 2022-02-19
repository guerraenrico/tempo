package com.enricog.navigation.api

import androidx.navigation.NavOptions

sealed class NavigationAction {
    data class GoTo(val route: String, val navOptions: NavOptions?) : NavigationAction()
    object GoBack : NavigationAction()
}
