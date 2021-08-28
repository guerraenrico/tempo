package com.enricog.navigation

import androidx.navigation.NavOptions

sealed class NavigationAction {
    data class GoTo(val route: String, val navOptions: NavOptions?) : NavigationAction()
    object GoBack : NavigationAction()
}
