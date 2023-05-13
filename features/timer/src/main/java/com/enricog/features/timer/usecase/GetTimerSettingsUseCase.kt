package com.enricog.features.timer.usecase

import com.enricog.data.timer.api.settings.TimerSettingsDataSource
import com.enricog.data.timer.api.settings.entities.TimerSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetTimerSettingsUseCase @Inject constructor(
    private val timerSettingsDataSource: TimerSettingsDataSource
) {

    operator fun invoke(): Flow<TimerSettings> {
        return timerSettingsDataSource.observe()
    }
}