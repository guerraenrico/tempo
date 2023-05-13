package com.enricog.data.local.database.timer.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.enricog.data.timer.api.settings.TimerSettingsDataSource
import com.enricog.data.timer.api.settings.entities.TimerSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class TimerSettingsDataSourceImpl @Inject constructor(
    @ApplicationContext val context: Context
) : TimerSettingsDataSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = TIMER_SETTINGS_DATA_STORE_PREF_NAME)

    private val soundEnabledKey = booleanPreferencesKey(SOUND_ENABLED_PREF_NAME)
    private val runInBackgroundEnabledKey = booleanPreferencesKey(RUN_IN_BACKGROUND_ENABLED_PREF_NAME)

    override fun observe(): Flow<TimerSettings> {
        return context.dataStore.data.map { preferences ->
            preferences.toTimerSettings()
        }
    }

    override suspend fun update(settings: TimerSettings) {
        context.dataStore.edit { preferences ->
            preferences[soundEnabledKey] = settings.soundEnabled
            preferences[runInBackgroundEnabledKey] = settings.runInBackgroundEnabled
        }
    }

    private fun Preferences.toTimerSettings(): TimerSettings {
        return TimerSettings(
            soundEnabled = this[soundEnabledKey] ?: true,
            runInBackgroundEnabled = this[runInBackgroundEnabledKey] ?: false
        )
    }

    private companion object {
        const val TIMER_SETTINGS_DATA_STORE_PREF_NAME = "timer_settings"

        const val SOUND_ENABLED_PREF_NAME = "sound_enabled"
        const val RUN_IN_BACKGROUND_ENABLED_PREF_NAME = "run_in_background_enabled"
    }
}