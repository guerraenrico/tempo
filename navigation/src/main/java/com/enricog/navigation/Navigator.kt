package com.enricog.navigation

import kotlinx.coroutines.flow.Flow

interface Navigator {
    val actions: Flow<NavigationAction>

    suspend fun navigate(action: NavigationAction)
}
