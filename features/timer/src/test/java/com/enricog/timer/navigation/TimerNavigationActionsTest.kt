package com.enricog.timer.navigation

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.navigation.api.Navigator
import com.enricog.navigation.api.routes.RoutinesRoute
import com.enricog.navigation.api.routes.RoutinesRouteInput
import com.enricog.navigation.api.routes.TimerRoute
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class TimerNavigationActionsTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val navigator: Navigator = mockk(relaxUnitFun = true)

    private val sut = TimerNavigationActions(navigator)

    @Test
    fun `test backToRoutines`() = coroutineRule {
        val expected = RoutinesRoute.navigate(RoutinesRouteInput) {
            popUpTo(TimerRoute.name) { inclusive = true }
        }

        sut.backToRoutines()

        coVerify { navigator.navigate(expected) }
    }
}