package com.enricog.data.local.converter

import androidx.room.TypeConverter
import com.enricog.data.routines.api.entities.TimeType

internal object TimeTypeConverter {
    @TypeConverter
    @JvmStatic
    fun fromTimeType(timeType: TimeType): String {
        return timeType.name
    }

    @TypeConverter
    @JvmStatic
    fun toTimeType(timeTypeName: String): TimeType {
        return TimeType.valueOf(timeTypeName)
    }
}
