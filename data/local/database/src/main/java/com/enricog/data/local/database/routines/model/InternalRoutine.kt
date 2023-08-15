package com.enricog.data.local.database.routines.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import com.enricog.core.entities.seconds
import com.enricog.data.local.database.routines.model.InternalRoutine.InternalFrequencyGoal
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.sortedByRank
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Entity(tableName = "Routines")
internal data class InternalRoutine(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "routineId") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "preparationTimeInSeconds") val preparationTime: Long,
    @ColumnInfo(name = "createdAt") val createdAt: OffsetDateTime,
    @ColumnInfo(name = "updatedAt") val updatedAt: OffsetDateTime,
    @ColumnInfo(name = "rank") val rank: String,
    @ColumnInfo(name = "rounds") val rounds: Int,
    @ColumnInfo(name = "frequencyGoal") val frequencyGoal: InternalFrequencyGoal?
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
            rounds = rounds,
            frequencyGoal = frequencyGoal?.toEntity()
        )
    }

    @Serializable
    data class InternalFrequencyGoal(
        val times: Int,
        val period: InternalPeriod
    ) {

        fun toEntity(): FrequencyGoal {
            return FrequencyGoal(
                times = times,
                period = period.toEntity()
            )
        }

        @Serializable
        enum class InternalPeriod {
            DAY, WEEK, MONTH;

            fun toEntity(): FrequencyGoal.Period {
                return when (this) {
                    DAY -> FrequencyGoal.Period.DAY
                    WEEK -> FrequencyGoal.Period.WEEK
                    MONTH -> FrequencyGoal.Period.MONTH
                }
            }
        }
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
        rounds = rounds,
        frequencyGoal = frequencyGoal?.toInternal()
    )
}

internal fun FrequencyGoal.toInternal(): InternalFrequencyGoal {
    return InternalFrequencyGoal(
        times = times,
        period = period.toInternal()
    )
}

private fun FrequencyGoal.Period.toInternal(): InternalFrequencyGoal.InternalPeriod {
    return when (this) {
        FrequencyGoal.Period.DAY -> InternalFrequencyGoal.InternalPeriod.DAY
        FrequencyGoal.Period.WEEK -> InternalFrequencyGoal.InternalPeriod.WEEK
        FrequencyGoal.Period.MONTH -> InternalFrequencyGoal.InternalPeriod.MONTH
    }
}
