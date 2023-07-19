package com.enricog.data.local.database.routines.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.sortedByRank
import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import com.enricog.core.entities.seconds
import java.time.OffsetDateTime

@Entity(tableName = "Routines")
internal data class InternalRoutine(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "routineId") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "preparationTimeInSeconds") val preparationTime: Long,
    @ColumnInfo(name = "createdAt") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updatedAt") val updatedAt: OffsetDateTime,
    @ColumnInfo(name = "rank") val rank: String,
    @ColumnInfo(name = "rounds") val rounds: Int
) {
    fun toEntity(segments: List<InternalSegment>): Routine {
        return Routine(
            id = ID.from(id),
            name = name,
            preparationTime = preparationTime.seconds,
            createdAt = createdAt,
            updatedAt = updatedAt,
            segments = segments
                .map { it.toEntity() }
                .sortedByRank(),
            rank = Rank.from(rank),
            rounds = rounds
        )
    }
}

internal fun Routine.toInternal(): InternalRoutine {
    return InternalRoutine(
        id = id.toLong(),
        name = name,
        preparationTime = preparationTime.value,
        createdAt = createdAt,
        updatedAt = updatedAt,
        rank = rank.toString(),
        rounds = rounds
    )
}
