package com.enricog.libraries.permission.api.notification

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.libraries.permission.api.PermissionNavigationActions
import com.enricog.navigation.testing.FakeNavigator
import org.junit.Rule
import org.junit.Test

internal class NotificationRationaleViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val navigator = FakeNavigator()

    @Test
    fun `should navigate back when cancel button is clicked`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.onCancel()

        navigator.assertGoBack()
    }

    @Test
    fun `should navigate back when continue button is clicked`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.onContinue()

        navigator.assertGoBack()
    }

    private fun buildViewModel(): NotificationRationaleViewModel {
        return NotificationRationaleViewModel(
            navigationActions = PermissionNavigationActions(navigator = navigator)
        )
    }
}
