package com.enricog.data.timer.testing.settings

import com.enricog.data.local.testing.FakeStore
import com.enricog.data.timer.api.settings.TimerSettingsDataSource
import com.enricog.data.timer.api.settings.entities.TimerSettings
import kotlinx.coroutines.flow.Flow

class FakeTimerSettingsDataSource(
    private val store: FakeStore<TimerSettings>
): TimerSettingsDataSource {


    override fun observe(): Flow<TimerSettings> {
        return store.asFlow()
    }

    override suspend fun update(settings: TimerSettings) {
        store.update { settings }
    }
}