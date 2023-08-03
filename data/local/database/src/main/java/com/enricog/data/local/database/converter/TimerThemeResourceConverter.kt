package com.enricog.data.local.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.enricog.data.local.database.timer.theme.model.InternalTimerTheme.InternalResource
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
internal class TimerThemeResourceConverter(private val json: Json) {

    @TypeConverter
    fun fromResource(resource: InternalResource): String {
        return json.encodeToString(resource)
    }

    @TypeConverter
    fun toResource(resourceJson: String): InternalResource {
        return json.decodeFromString(resourceJson)
    }
}
