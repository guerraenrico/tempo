package com.enricog.data.local.database.timer.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class TimerSettingsDataSourceImplTest {

    private val testCoroutineDispatcher = UnconfinedTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher + Job())

    @get:Rule
    val coroutineRule = CoroutineRule(mainTestDispatcher = testCoroutineDispatcher)

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testCoroutineScope,
        produceFile = { testContext.preferencesDataStoreFile("test_datastore") }
    )

    private val dataSource = TimerSettingsDataSourceImpl(dataStore = testDataStore)

    @After
    fun cleanUp() {
        testCoroutineScope.runTest {
            testDataStore.edit { it.clear() }
        }
        testCoroutineScope.cancel()
    }

    @Test
    fun shouldReturnDefaultSettingsWhenThereIsNoStoredData() = coroutineRule {
        val expected = TimerSettings(
            soundEnabled = true,
            runInBackgroundEnabled = false
        )

        dataSource.observe().test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun shouldReturnStoredSettingsWhenThereIsStoredData() = coroutineRule {
        testDataStore.edit { preferences ->
            preferences[booleanPreferencesKey(name = "sound_enabled")] = false
            preferences[booleanPreferencesKey(name = "run_in_background_enabled")] = true
        }
        val expected = TimerSettings(
            soundEnabled = false,
            runInBackgroundEnabled = true
        )

        dataSource.observe().test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun shouldUpdateStoredSettings() = coroutineRule {
        testDataStore.edit { preferences ->
            preferences[booleanPreferencesKey(name = "sound_enabled")] = false
            preferences[booleanPreferencesKey(name = "run_in_background_enabled")] = false
        }
        val settings = TimerSettings(
            soundEnabled = true,
            runInBackgroundEnabled = true
        )
        val expected = preferencesOf(
            booleanPreferencesKey(name = "sound_enabled") to true,
            booleanPreferencesKey(name = "run_in_background_enabled") to true
        )

        dataSource.update(settings = settings)

        testDataStore.data.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }
}