package com.enricog.libraries.permission.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.enricog.libraries.permission.api.notification.NotificationPermissionRoute
import com.enricog.libraries.permission.api.notification.NotificationRationaleScreen
import com.enricog.navigation.api.extensions.navViewModel
import com.enricog.libraries.permission.api.notification.NotificationPermissionRoute.compose as composeNotificationPermission

fun NavGraphBuilder.PermissionNavigation() {
    navigation(startDestination = NotificationPermissionRoute.name, route = "permission") {
        composeNotificationPermission { NotificationRationaleScreen(viewModel = navViewModel(it)) }
    }
}
