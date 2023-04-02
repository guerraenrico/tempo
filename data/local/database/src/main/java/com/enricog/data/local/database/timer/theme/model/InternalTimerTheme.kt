package com.enricog.data.local.database.timer.theme.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.enricog.core.entities.ID
import com.enricog.data.timer.api.theme.entities.TimerTheme
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Entity(tableName = "TimerThemes")
internal data class InternalTimerTheme(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "timerThemeId") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "preparationTimeResource") val preparationTimeResource: InternalResource,
    @ColumnInfo(name = "stopwatchResource") val stopwatchResource: InternalResource,
    @ColumnInfo(name = "timerResource") val timerResource: InternalResource,
    @ColumnInfo(name = "restResource") val restResource: InternalResource,
    @ColumnInfo(name = "isLocked") val isLocked: Boolean,
    @ColumnInfo(name = "isDefault") val isDefault: Boolean,
    @ColumnInfo(name = "createdAt") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updatedAt") val updatedAt: OffsetDateTime
) {

    fun toEntity(): TimerTheme {
        return TimerTheme(
            id = ID.from(value = id),
            name = name,
            description = description,
            preparationTimeResource = preparationTimeResource.toEntity(),
            stopwatchResource = stopwatchResource.toEntity(),
            timerResource = timerResource.toEntity(),
            restResource = restResource.toEntity(),
            isLocked = isLocked
        )
    }

    @Serializable
    data class InternalResource(
        val background: InternalAsset,
        val onBackground: InternalAsset.Color
    ) {

        fun toEntity(): TimerTheme.Resource {
            return TimerTheme.Resource(
                background = background.toEntity(),
                onBackground = onBackground.toEntity() as TimerTheme.Asset.Color
            )
        }
    }

    @Serializable
    sealed class InternalAsset {

        @Serializable
        @SerialName("color")
        data class Color(val argb: Long) : InternalAsset()

        fun toEntity(): TimerTheme.Asset {
            return when (this) {
                is Color -> TimerTheme.Asset.Color(argb = argb)
            }
        }
    }
}
