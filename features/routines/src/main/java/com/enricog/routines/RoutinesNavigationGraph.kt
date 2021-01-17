package com.enricog.routines

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigation
import com.enricog.routines.detail.routine.RoutineScreen
import com.enricog.routines.detail.segment.SegmentScreen
import com.enricog.routines.detail.summary.RoutineSummaryScreen
import com.enricog.routines.list.RoutinesScreen
import com.enricog.routines.navigation.RoutinesNavigationConstants.Routine
import com.enricog.routines.navigation.RoutinesNavigationConstants.RoutineSummary
import com.enricog.routines.navigation.RoutinesNavigationConstants.Routines
import com.enricog.routines.navigation.RoutinesNavigationConstants.Segment

fun NavGraphBuilder.RoutinesNavigation() {
    navigation(
        startDestination = Routines.routeName,
        route = "routinesNav"
    ) {
        composable(
            route = Routines.routeName,
            arguments = emptyList()
        ) {
            RoutinesScreen()
        }

        composable(
            route = RoutineSummary.routeName,
            arguments = listOf(navArgument(RoutineSummary.routeIdParamName) {
                type = NavType.LongType; defaultValue = 0L
            })
        ) { navBackStackEntry ->
            RoutineSummaryScreen(navBackStackEntry.arguments!!.getLong(Routine.routeIdParamName))
        }

        composable(
            route = Routine.routeName,
            arguments = listOf(navArgument(Routine.routeIdParamName) {
                type = NavType.LongType; defaultValue = 0L
            })
        ) { navBackStackEntry ->
            RoutineScreen(navBackStackEntry.arguments!!.getLong(Routine.routeIdParamName))
        }

        composable(
            route = Segment.routeName,
            arguments = listOf(
                navArgument(Segment.routeIdParamName) {
                    type = NavType.LongType; defaultValue = 0L
                },
                navArgument(Segment.segmentIdParamName) {
                    type = NavType.LongType; defaultValue = 0L
                }
            )
        ) { navBackStackEntry ->
            SegmentScreen(
                navBackStackEntry.arguments!!.getLong(Segment.routeIdParamName),
                navBackStackEntry.arguments!!.getLong(Segment.segmentIdParamName),
            )
        }
    }
}
