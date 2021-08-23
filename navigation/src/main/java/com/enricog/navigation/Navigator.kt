package com.enricog.navigation

interface Navigator {
    fun goBack()
    fun goTo(routeNavigation: RouteNavigation)
}
