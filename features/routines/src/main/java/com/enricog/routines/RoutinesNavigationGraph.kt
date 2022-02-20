package com.enricog.routines

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.enricog.navigation.api.extensions.navViewModel
import com.enricog.navigation.api.routes.RoutinesRoute
import com.enricog.routines.detail.routine.RoutineScreen
import com.enricog.routines.detail.segment.SegmentScreen
import com.enricog.routines.detail.summary.RoutineSummaryScreen
import com.enricog.routines.list.RoutinesScreen
import com.enricog.navigation.api.routes.RoutineRoute.compose as composeRoutine
import com.enricog.navigation.api.routes.RoutineSummaryRoute.compose as composeRoutineSummary
import com.enricog.navigation.api.routes.RoutinesRoute.compose as composeRoutines
import com.enricog.navigation.api.routes.SegmentRoute.compose as composeSegment

fun NavGraphBuilder.RoutinesNavigation() {
    navigation(
        startDestination = RoutinesRoute.name,
        route = "routinesNav"
    ) {
        composeRoutines {
            RoutinesScreen(viewModel = navViewModel(it))
        }

        composeRoutineSummary {
            RoutineSummaryScreen(viewModel = navViewModel(it))
        }

        composeRoutine {
            RoutineScreen(viewModel = navViewModel(it))
        }

        composeSegment {
            SegmentScreen(viewModel = navViewModel(it))
        }
    }
}
