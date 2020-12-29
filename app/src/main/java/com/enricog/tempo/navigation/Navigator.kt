package com.enricog.tempo.navigation

import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import androidx.navigation.compose.popUpTo
import com.enricog.routines.navigation.RoutinesNavigationActions
import com.enricog.routines.navigation.RoutinesNavigationConstants
import com.enricog.timer.navigation.TimerNavigationActions
import com.enricog.timer.navigation.TimerNavigationConstants
import javax.inject.Inject

internal class Navigator @Inject constructor() : RoutinesNavigationActions, TimerNavigationActions {

    var navController: NavController? = null

    // RoutinesNavigationActions

    override fun goToRoutines() {
        navController?.navigate(RoutinesNavigationConstants.Routines.routeName)
    }

    override fun goToRoutine(routineId: Long?) {
        navController?.navigate(RoutinesNavigationConstants.Routine.applyRouteId(routineId))
    }

    override fun goToTimer(routineId: Long) {
        navController?.navigate(TimerNavigationConstants.applyRouteId(routineId)) {
            popUpTo("routines") { inclusive = true }
        }
    }

    // TimerNavigationActions

    override fun backToRoutines() {
        navController?.navigate(RoutinesNavigationConstants.Routines.routeName) {
            popUpTo(TimerNavigationConstants.routeName) { inclusive = true }
        }
    }

}