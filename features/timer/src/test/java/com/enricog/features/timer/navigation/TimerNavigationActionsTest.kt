package com.enricog.features.timer.navigation

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.navigation.api.routes.RoutinesRoute
import com.enricog.navigation.api.routes.RoutinesRouteInput
import com.enricog.navigation.testing.FakeNavigator
import org.junit.Rule
import org.junit.Test

class TimerNavigationActionsTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val navigator = FakeNavigator()

    private val navigationActions = TimerNavigationActions(navigator)

    @Test
    fun `test backToRoutines`() = coroutineRule {
        navigationActions.backToRoutines()

        navigator.assertGoTo(RoutinesRoute, RoutinesRouteInput)
    }
}