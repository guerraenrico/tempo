package com.enricog.data.local.converter

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.time.OffsetDateTime

internal object OffsetDateTimeConverter {
    @TypeConverter
    @JvmStatic
    fun fromDateTime(offsetDateTime: OffsetDateTime): String {
        return offsetDateTime.toString()
    }

    @SuppressLint("NewApi")
    @TypeConverter
    @JvmStatic
    fun toDateTime(offsetDateTime: String): OffsetDateTime {
        return OffsetDateTime.parse(offsetDateTime)
    }
}
