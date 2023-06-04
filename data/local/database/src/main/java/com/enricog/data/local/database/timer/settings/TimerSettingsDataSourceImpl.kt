package com.enricog.data.local.database.timer.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.enricog.data.timer.api.settings.TimerSettingsDataSource
import com.enricog.data.timer.api.settings.entities.TimerSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

internal class TimerSettingsDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TimerSettingsDataSource {

    private val keepScreenOnEnabledKey = booleanPreferencesKey(KEEP_SCREEN_ON_ENABLED_PREF_NAME)
    private val soundEnabledKey = booleanPreferencesKey(SOUND_ENABLED_PREF_NAME)
    private val runInBackgroundEnabledKey = booleanPreferencesKey(RUN_IN_BACKGROUND_ENABLED_PREF_NAME)

    override fun observe(): Flow<TimerSettings> {
        return dataStore.data
            .catch { throwable ->
                if (throwable is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw throwable
                }
            }
            .map { preferences ->
                preferences.toTimerSettings()
            }
    }

    override suspend fun update(settings: TimerSettings) {
        dataStore.edit { preferences ->
            preferences[keepScreenOnEnabledKey] = settings.keepScreenOnEnabled
            preferences[soundEnabledKey] = settings.soundEnabled
            preferences[runInBackgroundEnabledKey] = settings.runInBackgroundEnabled
        }
    }

    private fun Preferences.toTimerSettings(): TimerSettings {
        return TimerSettings(
            keepScreenOnEnabled = this[keepScreenOnEnabledKey] ?: true,
            soundEnabled = this[soundEnabledKey] ?: true,
            runInBackgroundEnabled = this[runInBackgroundEnabledKey] ?: false
        )
    }

    private companion object {
        const val KEEP_SCREEN_ON_ENABLED_PREF_NAME = "keep_screen_on_enabled"
        const val SOUND_ENABLED_PREF_NAME = "sound_enabled"
        const val RUN_IN_BACKGROUND_ENABLED_PREF_NAME = "run_in_background_enabled"
    }
}