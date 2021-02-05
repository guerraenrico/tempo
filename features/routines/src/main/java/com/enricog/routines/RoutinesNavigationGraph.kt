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
import com.enricog.ui_components.ambients.navViewModel

fun NavGraphBuilder.RoutinesNavigation() {
    navigation(
        startDestination = Routines.routeName,
        route = "routinesNav"
    ) {
        composable(
            route = Routines.routeName,
            arguments = emptyList()
        ) {
            RoutinesScreen(
                viewModel = navViewModel(it)
            )
        }

        composable(
            route = RoutineSummary.routeName,
            arguments = listOf(
                navArgument(RoutineSummary.routeIdParamName) {
                    type = NavType.LongType; defaultValue = 0L
                }
            )
        ) {
            RoutineSummaryScreen(
                routineId = it.arguments!!.getLong(Routine.routeIdParamName),
                viewModel = navViewModel(it)
            )
        }

        composable(
            route = Routine.routeName,
            arguments = listOf(
                navArgument(Routine.routeIdParamName) {
                    type = NavType.LongType; defaultValue = 0L
                }
            )
        ) {
            RoutineScreen(
                routineId = it.arguments!!.getLong(Routine.routeIdParamName),
                viewModel = navViewModel(it)
            )
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
        ) {
            SegmentScreen(
                routineId = it.arguments!!.getLong(Segment.routeIdParamName),
                segmentId = it.arguments!!.getLong(Segment.segmentIdParamName),
                viewModel = navViewModel(it)
            )
        }
    }
}
