package com.enricog.timer.navigation

interface TimerNavigationActions {
    fun backToRoutines()
}

object TimerNavigationConstants {
    const val routeIdParamName = "routineId"

    const val routeName = "timer/{$routeIdParamName}"

    fun applyRouteId(routineId: Long): String {
        return "timer/$routineId"
    }
}