package com.enricog.routines.navigation

interface RoutinesNavigationActions {
    fun goBackToRoutines()
    fun goToRoutineSummary(routineId: Long)
    fun goToRoutine(routineId: Long?)
    fun goToSegment(routineId: Long, segmentId: Long?)
    fun goToTimer(routineId: Long)
}

object RoutinesNavigationConstants {
    object Routines {
        const val routeName = "routines"
    }

    object RoutineSummary {
        const val routeIdParamName = "routineId"

        const val routeName = "routine/${routeIdParamName}"

        fun applyRouteId(routineId: Long): String {
            val routeName = StringBuffer("routine")
            routeName.append("/$routineId")
            return routeName.toString()
        }
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

    object Segment {
        const val routeIdParamName = "routineId"
        const val segmentIdParamName = "segmentId"

        const val routeName =
            "segment?routineId={$routeIdParamName}&segmentId=${segmentIdParamName}"

        fun applyRouteId(routineId: Long, segmentId: Long?): String {
            val routeName = StringBuffer("routine")
            routeName.append("?routineId=$routineId")
            if (segmentId != null) {
                routeName.append("&segmentId=$segmentId")
            }
            return routeName.toString()
        }
    }
}