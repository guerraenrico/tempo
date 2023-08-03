package com.enricog.data.local.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.enricog.data.local.database.routines.model.InternalRoutine.InternalFrequencyGoal
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ProvidedTypeConverter
internal class FrequencyGoalConverter(private val json: Json) {

    @TypeConverter
    fun fromFrequencyGoal(frequencyGoal: InternalFrequencyGoal?): String? {
        return frequencyGoal?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toFrequencyGoal(frequencyGoalJson: String?): InternalFrequencyGoal? {
        return frequencyGoalJson?.let { json.decodeFromString(it) }
    }
}
