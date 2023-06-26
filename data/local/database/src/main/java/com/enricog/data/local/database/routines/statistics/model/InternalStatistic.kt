package com.enricog.data.local.database.routines.statistics.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.enricog.core.entities.ID
import com.enricog.core.entities.seconds
import com.enricog.data.local.database.routines.model.InternalRoutine
import com.enricog.data.routines.api.statistics.entities.Statistic
import java.time.OffsetDateTime

@Entity(
    tableName = "RoutineStatistics",
    foreignKeys = [
        ForeignKey(
            entity = InternalRoutine::class,
            parentColumns = ["routineId"],
            childColumns = ["routineId_fk"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["routineId_fk"], unique = false)]
)
internal data class InternalStatistic(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "statisticId") val id: Long,
    @ColumnInfo(name = "routineId_fk") val routineId: Long,
    @ColumnInfo(name = "createdAt") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "effectiveTime") val effectiveTime: Long
) {

    fun toEntity(): Statistic {
        return Statistic(
            id = ID.from(id),
            routineId = ID.from(routineId),
            createdAt = createdAt,
            type = Statistic.Type.valueOf(type),
            effectiveTime = effectiveTime.seconds
        )
    }
}

internal fun Statistic.toInternal(): InternalStatistic {
    return InternalStatistic(
        id = id.toLong(),
        routineId = routineId.toLong(),
        createdAt = createdAt,
        type = type.name,
        effectiveTime = effectiveTime.value
    )
}
