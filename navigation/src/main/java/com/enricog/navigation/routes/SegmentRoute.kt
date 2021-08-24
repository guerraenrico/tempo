package com.enricog.navigation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navOptions
import com.enricog.navigation.NavigationAction
import com.enricog.navigation.routes.SegmentRoute.Params.routeId
import com.enricog.navigation.routes.SegmentRoute.Params.segmentId

object SegmentRoute : Route<SegmentRouteInput> {
    private object Params {
        const val routeId = "routineId"
        const val segmentId = "segmentId"
    }

    override val name = "routine/{$routeId}/segment/edit?segmentId={$segmentId}"

    override fun NavGraphBuilder.compose(content: @Composable (NavBackStackEntry) -> Unit) {
        composable(
            route = name,
            arguments = listOf(
                navArgument(routeId) {
                    type = NavType.LongType; defaultValue = 0L
                },
                navArgument(segmentId) {
                    type = NavType.LongType; defaultValue = 0L
                }
            ),
            content = content
        )
    }

    override fun navigate(
        input: SegmentRouteInput,
        optionsBuilder: (NavOptionsBuilder.() -> Unit)?
    ): NavigationAction {
        val route = buildString {
            append("routine/${input.routineId}/segment/edit")
            if (input.segmentId != null) {
                append("?segmentId=${input.segmentId}")
            }
        }
        val options = optionsBuilder?.let { navOptions(it) }
        return NavigationAction.GoTo(route = route, navOptions = options)
    }
}

class SegmentRouteInput(val routineId: Long, val segmentId: Long?) : RouteInput
