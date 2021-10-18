package com.enricog.navigation.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import com.enricog.entities.ID
import com.enricog.navigation.NavigationAction
import com.enricog.navigation.routes.SegmentRoute.Params.routineId
import com.enricog.navigation.routes.SegmentRoute.Params.segmentId

object SegmentRoute : Route<SegmentRouteInput> {
    private object Params {
        const val routineId = "routineId"
        const val segmentId = "segmentId"
    }

    override val name = "routine/{$routineId}/segment/edit?segmentId={$segmentId}"

    override fun NavGraphBuilder.compose(content: @Composable (NavBackStackEntry) -> Unit) {
        composable(
            route = name,
            arguments = listOf(
                navArgument(routineId) {
                    type = NavType.LongType
                },
                navArgument(segmentId) {
                    type = NavType.LongType
                }
            ),
            content = content
        )
    }

    override fun navigate(
        input: SegmentRouteInput,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ): NavigationAction {
        val route = "routine/${input.routineId.toLong()}/segment/edit?segmentId=${input.segmentId.toLong()}"
        val options = optionsBuilder?.let { navOptions(it) }
        return NavigationAction.GoTo(route = route, navOptions = options)
    }

    override fun extractInput(savedStateHandle: SavedStateHandle): SegmentRouteInput {
        val routineId = ID.from(savedStateHandle.get<Long>(routineId)!!)
        val segmentId = ID.from(savedStateHandle.get<Long>(segmentId)!!)
        return SegmentRouteInput(
            routineId = routineId,
            segmentId = segmentId
        )
    }
}

data class SegmentRouteInput(val routineId: ID, val segmentId: ID) : RouteInput
