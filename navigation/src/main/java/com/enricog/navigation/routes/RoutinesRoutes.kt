package com.enricog.navigation.routes

import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavType
import com.enricog.navigation.R
import com.enricog.navigation.baseUri

sealed class RoutinesRoutes : Route {

    object Routines : RoutinesRoutes() {

        override val routeName: String = "routines"

        override val arguments: List<Pair<String, NavArgumentBuilder.() -> Unit>> = emptyList()

        override val deepLinkPatterns: List<String> = listOf(
            "$baseUri/routines"
        )
    }

    object Routine : RoutinesRoutes() {

        private const val routineId = "routineId"

        override val routeName: String = "routine"

        override val arguments: List<Pair<String, NavArgumentBuilder.() -> Unit>> = listOf(
            routineId to { type = NavType.LongType; nullable = true; defaultValue = null }
        )

        override val deepLinkPatterns: List<String> = listOf(
            "$baseUri/routines/{$routineId}"
        )


    }

}