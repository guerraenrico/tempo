package com.enricog.libraries.notification.api

import com.enricog.libraries.notification.api.notification.NotificationPermissionRoute
import com.enricog.navigation.api.NavigationAction
import com.enricog.navigation.api.Navigator
import javax.inject.Inject

internal class PermissionNavigationActions @Inject constructor(
    private val navigator: Navigator
) {

    suspend fun openNotificationRationale() {
        val routeNavigation = NotificationPermissionRoute.navigate(
            input = NotificationPermissionRoute.Input,
            optionsBuilder = null
        )
        navigator.navigate(routeNavigation)
    }

    suspend fun back() {
        navigator.navigate(NavigationAction.GoBack)
    }
}