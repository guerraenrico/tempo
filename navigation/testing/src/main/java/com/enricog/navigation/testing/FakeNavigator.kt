package com.enricog.navigation.testing

import app.cash.turbine.test
import com.enricog.navigation.api.NavigationAction
import com.enricog.navigation.api.Navigator
import com.enricog.navigation.api.routes.Route
import com.enricog.navigation.api.routes.RouteInput
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

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

    suspend fun <I: RouteInput> assertGoTo(route: Route<I>, input: I) {
        _actions.test {
            val item = awaitItem()
            assertTrue(
                actual = item is NavigationAction.GoTo,
                message = "Expected ${NavigationAction.GoTo::class.java.simpleName} got $item"
            )
            val expected = route.navigate(input = input, optionsBuilder = null) as NavigationAction.GoTo
            assertEquals(expected.route, item.route)
        }
    }

    suspend fun assertGoBack() {
        _actions.test {
            val item = awaitItem()
            assertTrue(
                actual = item is NavigationAction.GoBack,
                message = "Expected ${NavigationAction.GoBack::class.java.simpleName} got $item"
            )
        }
    }

    suspend fun assertNoActions() {
        _actions.test { expectNoEvents() }
    }
}