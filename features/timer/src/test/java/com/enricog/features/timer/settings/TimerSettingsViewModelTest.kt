package com.enricog.features.timer.settings

import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.data.timer.testing.settings.FakeTimerSettingsDataSource
import com.enricog.data.timer.testing.settings.entities.DEFAULT
import com.enricog.features.timer.R
import com.enricog.features.timer.settings.models.TimerSettingsViewState
import com.enricog.features.timer.usecase.GetTimerSettingsUseCase
import com.enricog.features.timer.usecase.UpdateTimerSettingsUseCase
import com.enricog.libraries.permission.api.Permission
import com.enricog.libraries.permission.testing.FakePermissionManager
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


internal class TimerSettingsViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val timerSettingsDataSource = FakeTimerSettingsDataSource(
        store = FakeStore(TimerSettings.DEFAULT)
    )
    private val permissionManager = FakePermissionManager()

    @Before
    fun setup() {
        permissionManager.setPermission(Permission.NOTIFICATION to true)
    }

    @Test
    fun `should get initial settings state`() = coroutineRule {
        val expected = TimerSettingsViewState.Data(
            soundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_sound,
                enabled = true
            ),
            runInBackgroundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_background,
                enabled = false
            )
        )

        val viewModel = buildViewModel()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should update sound setting when sound is toggled`() = coroutineRule {
        val expected = TimerSettingsViewState.Data(
            soundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_sound,
                enabled = false
            ),
            runInBackgroundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_background,
                enabled = false
            )
        )
        val viewModel = buildViewModel()

        viewModel.onToggleSound()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should update run in background setting when run in background is toggled`() = coroutineRule {
        val expected = TimerSettingsViewState.Data(
            soundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_sound,
                enabled = true
            ),
            runInBackgroundSetting = TimerSettingsViewState.Data.Setting(
                title = R.string.label_routine_settings_background,
                enabled = true
            )
        )
        val viewModel = buildViewModel()

        viewModel.onToggleRunInBackground()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    private fun buildViewModel(): TimerSettingsViewModel {
        return TimerSettingsViewModel(
            dispatchers = coroutineRule.getDispatchers(),
            converter = TimerSettingsStateConverter(),
            reducer = TimerSettingsReducer(),
            getTimerSettingsUseCase = GetTimerSettingsUseCase(
                timerSettingsDataSource = timerSettingsDataSource,
                permissionManager = permissionManager
            ),
            updateTimerSettingsUseCase = UpdateTimerSettingsUseCase(
                timerSettingsDataSource = timerSettingsDataSource,
                permissionManager = permissionManager
            )
        )
    }
}
