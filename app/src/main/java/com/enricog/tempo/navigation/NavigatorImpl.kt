package com.enricog.tempo.navigation

import com.enricog.navigation.NavigationAction
import com.enricog.navigation.Navigator
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class NavigatorImpl @Inject constructor() : Navigator {

    private val _actions = MutableSharedFlow<NavigationAction>(
        extraBufferCapacity = 5,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    override val actions: Flow<NavigationAction> = _actions

    override suspend fun navigate(action: NavigationAction) {
        _actions.emit(action)
    }
}
