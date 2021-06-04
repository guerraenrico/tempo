package com.enricog.tempo.navigation

import androidx.navigation.NavController
import com.enricog.routines.navigation.RoutinesNavigationActions
import com.enricog.routines.navigation.RoutinesNavigationConstants
import com.enricog.routines.navigation.RoutinesNavigationConstants.Routine
import com.enricog.routines.navigation.RoutinesNavigationConstants.RoutineSummary
import com.enricog.routines.navigation.RoutinesNavigationConstants.Routines
import com.enricog.timer.navigation.TimerNavigationActions
import com.enricog.timer.navigation.TimerNavigationConstants
import javax.inject.Inject

internal class Navigator @Inject constructor() : RoutinesNavigationActions, TimerNavigationActions {

    var navController: NavController? = null

    // RoutinesNavigationActions

    override fun routinesBack() {
        navController?.popBackStack()
    }

    override fun goToRoutineSummary(routineId: Long) {
        navController?.navigate(RoutineSummary.applyRouteId(routineId)) {
            popUpTo(Routines.routeName) { inclusive = false }
        }
    }

    override fun goToRoutine(routineId: Long?) {
        navController?.navigate(Routine.applyRouteId(routineId))
    }

    override fun goToSegment(routineId: Long, segmentId: Long?) {
        navController?.navigate(
            RoutinesNavigationConstants.Segment.applyRouteId(
                routineId,
                segmentId
            )
        )
    }

    override fun goToTimer(routineId: Long) {
        navController?.navigate(TimerNavigationConstants.applyRouteId(routineId)) {
            popUpTo(Routines.routeName) { inclusive = true }
        }
    }

    // TimerNavigationActions

    override fun backToRoutines() {
        navController?.navigate(Routines.routeName) {
            popUpTo(TimerNavigationConstants.routeName) { inclusive = true }
        }
    }
}
