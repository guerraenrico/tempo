package com.enricog.features.timer.usecase

import com.enricog.data.timer.api.settings.TimerSettingsDataSource
import com.enricog.data.timer.api.settings.entities.TimerSettings
import javax.inject.Inject

internal class UpdateTimerSettingsUseCase @Inject constructor(
    private val timerSettingsDataSource: TimerSettingsDataSource
) {

    suspend operator fun invoke(settings: TimerSettings) {
        timerSettingsDataSource.update(settings = settings)
    }
}