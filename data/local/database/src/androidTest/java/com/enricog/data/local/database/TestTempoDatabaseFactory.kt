package com.enricog.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.enricog.data.local.database.converter.FrequencyGoalConverter
import com.enricog.data.local.database.converter.TimerThemeResourceConverter
import kotlinx.serialization.json.Json

internal object TestTempoDatabaseFactory {

    fun create(): TempoDatabase {
        val context: Context = ApplicationProvider.getApplicationContext()
        return Room.inMemoryDatabaseBuilder(context, TempoDatabase::class.java)
            .addTypeConverter(TimerThemeResourceConverter(json = Json))
            .addTypeConverter(FrequencyGoalConverter(json = Json))
            .allowMainThreadQueries()
            .build()
    }
}