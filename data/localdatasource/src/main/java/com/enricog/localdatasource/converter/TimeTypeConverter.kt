package com.enricog.localdatasource.converter

import androidx.room.TypeConverter
import com.enricog.entities.routines.TimeType

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
