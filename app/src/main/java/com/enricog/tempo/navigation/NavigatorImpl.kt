package com.enricog.tempo.navigation

import androidx.navigation.NavController
import com.enricog.navigation.Navigator
import com.enricog.navigation.RouteNavigation
import javax.inject.Inject

internal class NavigatorImpl @Inject constructor() : Navigator {

    var navController: NavController? = null

    override fun goBack() {
        navController?.popBackStack()
    }

    override fun goTo(routeNavigation: RouteNavigation) {
        navController?.navigate(
            route = routeNavigation.route,
            navOptions = routeNavigation.navOptions
        )
    }
}
