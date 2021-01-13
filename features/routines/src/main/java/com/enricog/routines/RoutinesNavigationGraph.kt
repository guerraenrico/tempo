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
import com.enricog.routines.navigation.RoutinesNavigationConstants

fun NavGraphBuilder.RoutinesNavigation() {
    navigation(
        startDestination = RoutinesNavigationConstants.Routines.routeName,
        route = "routinesNav"
    ) {
        composable(
            route = RoutinesNavigationConstants.Routines.routeName,
            arguments = emptyList()
        ) {
            RoutinesScreen()
        }

        composable(
            route = RoutinesNavigationConstants.RoutineSummary.routeName,
            arguments = listOf(navArgument(RoutinesNavigationConstants.RoutineSummary.routeIdParamName) {
                type = NavType.LongType
            })
        ) { navBackStackEntry ->
            RoutineSummaryScreen(navBackStackEntry.arguments!!.getLong(RoutinesNavigationConstants.Routine.routeIdParamName))
        }

        composable(
            route = RoutinesNavigationConstants.Routine.routeName,
            arguments = listOf(navArgument(RoutinesNavigationConstants.Routine.routeIdParamName) {
                type = NavType.LongType; defaultValue = 0L
            })
        ) { navBackStackEntry ->
            RoutineScreen(navBackStackEntry.arguments!!.getLong(RoutinesNavigationConstants.Routine.routeIdParamName))
        }

        composable(
            route = RoutinesNavigationConstants.Segment.routeName,
            arguments = listOf(
                navArgument(RoutinesNavigationConstants.Segment.routeIdParamName) {
                    type = NavType.LongType
                },
                navArgument(RoutinesNavigationConstants.Segment.segmentIdParamName) {
                    type = NavType.LongType; defaultValue = 0L
                }
            )
        ) { navBackStackEntry ->
            SegmentScreen(
                navBackStackEntry.arguments!!.getLong(RoutinesNavigationConstants.Segment.routeIdParamName),
                navBackStackEntry.arguments!!.getLong(RoutinesNavigationConstants.Segment.segmentIdParamName),
            )
        }
    }
}
