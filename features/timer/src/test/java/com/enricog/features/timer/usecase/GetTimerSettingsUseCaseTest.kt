package com.enricog.features.timer.usecase

import app.cash.turbine.test
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

internal class GetTimerSettingsUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val store = FakeStore(TimerSettings.DEFAULT)
    private val timerSettingsDataSource = FakeTimerSettingsDataSource(store = store)
    private val permissionManager = FakePermissionManager()

    private val useCase = GetTimerSettingsUseCase(
        timerSettingsDataSource = timerSettingsDataSource,
        permissionManager = permissionManager
    )

    @Test
    fun `should return timer settings with enable run in background when notification permission is given`() =
        coroutineRule {
            permissionManager.setPermission(Permission.NOTIFICATION to true)
            store.put(
                TimerSettings.DEFAULT.copy(
                    soundEnabled = true,
                    runInBackgroundEnabled = true
                )
            )
            val expected = TimerSettings.DEFAULT.copy(
                soundEnabled = true,
                runInBackgroundEnabled = true
            )

            val flow = useCase()

            flow.test { assertThat(awaitItem()).isEqualTo(expected) }
        }

    @Test
    fun `should return timer settings with disabled run in background when notification permission is not given`() =
        coroutineRule {
            permissionManager.setPermission(Permission.NOTIFICATION to false)
            store.put(
                TimerSettings.DEFAULT.copy(
                    soundEnabled = true,
                    runInBackgroundEnabled = true
                )
            )
            val expected = TimerSettings.DEFAULT.copy(
                soundEnabled = true,
                runInBackgroundEnabled = false
            )

            val flow = useCase()

            flow.test { assertThat(awaitItem()).isEqualTo(expected) }
        }
}