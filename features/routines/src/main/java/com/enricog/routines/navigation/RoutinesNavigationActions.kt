package com.enricog.routines.navigation

interface RoutinesNavigationActions {
    fun routinesBack()
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

        const val routeName = "routine/{$routeIdParamName}/summary"

        fun applyRouteId(routineId: Long): String {
            return "routine/$routineId/summary"
        }
    }

    object Routine {
        const val routeIdParamName = "routineId"

        const val routeName = "routine/edit?routineId={$routeIdParamName}"

        fun applyRouteId(routineId: Long?): String {
            return buildString {
                append("routine/edit")
                if (routineId != null) {
                    append("?routineId=$routineId")
                }
            }
        }
    }

    object Segment {
        const val routeIdParamName = "routineId"
        const val segmentIdParamName = "segmentId"

        const val routeName =
            "routine/{$routeIdParamName}/segment/edit?segmentId={$segmentIdParamName}"

        fun applyRouteId(routineId: Long, segmentId: Long?): String {
            return buildString {
                append("routine/$routineId/segment/edit")
                if (segmentId != null) {
                    append("?segmentId=$segmentId")
                }
            }
        }
    }
}