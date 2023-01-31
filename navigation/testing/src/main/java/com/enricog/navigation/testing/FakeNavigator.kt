package com.enricog.navigation.testing

import app.cash.turbine.test
import com.enricog.navigation.api.NavigationAction
import com.enricog.navigation.api.Navigator
import com.enricog.navigation.api.routes.Route
import com.enricog.navigation.api.routes.RouteInput
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeNavigator : Navigator {

    private val _actions = MutableSharedFlow<NavigationAction>(
        replay = 5,
        extraBufferCapacity = 5,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    override val actions: Flow<NavigationAction> = _actions

    override suspend fun navigate(action: NavigationAction) {
        _actions.emit(action)
    }

    suspend fun <I : RouteInput> assertGoTo(route: Route<I>, input: I) {
        _actions.test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(NavigationAction.GoTo::class.java)
            val expected = route.navigate(
                input = input,
                optionsBuilder = null
            ) as NavigationAction.GoTo
            val routine = (item as NavigationAction.GoTo).route
            assertThat(routine).isEqualTo(expected.route)
        }
    }

    suspend fun assertGoBack() {
        _actions.test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(NavigationAction.GoBack::class.java)
        }
    }

    suspend fun assertNoActions() {
        _actions.test { expectNoEvents() }
    }
}