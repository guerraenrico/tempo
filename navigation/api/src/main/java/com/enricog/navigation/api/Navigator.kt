package com.enricog.navigation.api

import kotlinx.coroutines.flow.Flow

interface Navigator {
    val actions: Flow<NavigationAction>

    suspend fun navigate(action: NavigationAction)
}
