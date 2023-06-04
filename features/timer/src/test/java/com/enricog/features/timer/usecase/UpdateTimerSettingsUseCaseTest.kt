package com.enricog.features.timer.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.testing.settings.FakeTimerSettingsDataSource
import com.enricog.data.timer.testing.settings.entities.DEFAULT
import com.enricog.libraries.permission.api.Permission
import com.enricog.libraries.permission.testing.FakePermissionManager
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

internal class UpdateTimerSettingsUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val initialTimerSettings = TimerSettings.DEFAULT
    private val store = FakeStore(initialTimerSettings)
    private val timerSettingsDataSource = FakeTimerSettingsDataSource(store = store)
    private val permissionManager = FakePermissionManager()

    private val useCase = UpdateTimerSettingsUseCase(
        timerSettingsDataSource = timerSettingsDataSource,
        permissionManager = permissionManager
    )

    @Test
    fun `should ask notification permission when run in background is enabled and permission is not given`() =
        coroutineRule {
            permissionManager.setPermission(Permission.NOTIFICATION to false)
            val timerSettings = TimerSettings.DEFAULT.copy(runInBackgroundEnabled = true)

            useCase(settings = timerSettings)

            permissionManager.isPermissionAsked(Permission.NOTIFICATION)
            assertThat(store.get()).isEqualTo(initialTimerSettings)
        }

    @Test
    fun `should update settings when run in background is enabled and permission is given`() =
        coroutineRule {
            permissionManager.setPermission(Permission.NOTIFICATION to true)
            val timerSettings = TimerSettings.DEFAULT.copy(runInBackgroundEnabled = true)

            useCase(settings = timerSettings)

            permissionManager.isPermissionNotAsked(Permission.NOTIFICATION)
            assertThat(store.get()).isEqualTo(timerSettings)
        }

    @Test
    fun `should update settings when sound is toggled`() =
        coroutineRule {
            val timerSettings = TimerSettings.DEFAULT.copy(soundEnabled = false)

            useCase(settings = timerSettings)

            permissionManager.isPermissionNotAsked(Permission.NOTIFICATION)
            assertThat(store.get()).isEqualTo(timerSettings)
        }
}
