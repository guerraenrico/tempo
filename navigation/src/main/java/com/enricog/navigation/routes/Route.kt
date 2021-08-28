package com.enricog.navigation.routes

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import com.enricog.navigation.NavigationAction

sealed interface Route<I : RouteInput> {
    val name: String

    fun NavGraphBuilder.compose(content: @Composable (NavBackStackEntry) -> Unit)

    fun navigate(input: I, optionsBuilder: (NavOptionsBuilder.() -> Unit)?): NavigationAction

    fun extractInput(savedStateHandle: SavedStateHandle): I
}
