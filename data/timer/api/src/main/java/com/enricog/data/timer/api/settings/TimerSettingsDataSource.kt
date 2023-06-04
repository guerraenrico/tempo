package com.enricog.data.timer.api.settings

import com.enricog.data.timer.api.settings.entities.TimerSettings
import kotlinx.coroutines.flow.Flow

interface TimerSettingsDataSource {
    fun observe(): Flow<TimerSettings>
    suspend fun update(settings: TimerSettings)
}
