package com.enricog.features.timer.settings

import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.features.timer.settings.models.TimerSettingsState
import com.enricog.features.timer.settings.models.TimerSettingsViewState
import com.enricog.features.timer.usecase.GetTimerSettingsUseCase
import com.enricog.features.timer.usecase.UpdateTimerSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class TimerSettingsViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    converter: TimerSettingsStateConverter,
    reducer: TimerSettingsReducer,
    getTimerSettingsUseCase: GetTimerSettingsUseCase,
    private val updateTimerSettingsUseCase: UpdateTimerSettingsUseCase
) : BaseViewModel<TimerSettingsState, TimerSettingsViewState>(
    initialState = TimerSettingsState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {

    init {
        getTimerSettingsUseCase()
            .onEach { timerSettings ->
                updateState { state ->
                    reducer.updateTimerSettings(state = state, timerSettings = timerSettings)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onToggleSound() {
        launchWhen<TimerSettingsState.Data> { state ->
            val updatedSettings = state.timerSettings.copy(
                soundEnabled = !state.timerSettings.soundEnabled
            )
            updateTimerSettingsUseCase(settings = updatedSettings)
        }
    }

    fun onToggleRunInBackground() {
        launchWhen<TimerSettingsState.Data> { state ->
            val updatedSettings = state.timerSettings.copy(
                runInBackgroundEnabled = !state.timerSettings.runInBackgroundEnabled
            )
            updateTimerSettingsUseCase(settings = updatedSettings)
        }
    }
}
