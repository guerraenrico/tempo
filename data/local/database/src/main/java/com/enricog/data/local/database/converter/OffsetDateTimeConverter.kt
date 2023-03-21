package com.enricog.data.local.database.converter

import androidx.room.TypeConverter
import java.time.OffsetDateTime

internal object OffsetDateTimeConverter {
    @TypeConverter
    @JvmStatic
    fun fromDateTime(offsetDateTime: OffsetDateTime): String {
        return offsetDateTime.toString()
    }

    @TypeConverter
    @JvmStatic
    fun toDateTime(offsetDateTime: String): OffsetDateTime {
        return OffsetDateTime.parse(offsetDateTime)
    }
}
