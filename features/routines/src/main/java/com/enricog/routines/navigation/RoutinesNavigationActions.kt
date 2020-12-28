package com.enricog.routines.navigation

interface RoutinesNavigationActions {
    fun goToRoutines()
    fun goToRoutine(routineId: Long?)
    fun goToTimer(routineId: Long)
}

object RoutinesNavigationConstants {
    object Routines {
        const val routeName = "routines"
    }

    object Routine {
        const val routeIdParamName = "routineId"

        const val routeName = "routine?routineId={$routeIdParamName}"

        fun applyRouteId(routineId: Long?): String {
            val routeName = StringBuffer("routine")
            if (routineId != null) {
                routeName.append("?routineId=$routineId")
            }
            return routeName.toString()
        }
    }
}