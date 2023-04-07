package com.enricog.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.enricog.data.local.database.converter.OffsetDateTimeConverter
import com.enricog.data.local.database.converter.TimeTypeConverter
import com.enricog.data.local.database.converter.TimerThemeResourceConverter
import com.enricog.data.local.database.migrations.MigrationFrom1To2
import com.enricog.data.local.database.migrations.MigrationFrom2To3
import com.enricog.data.local.database.migrations.MigrationFrom3To4
import com.enricog.data.local.database.routines.dao.RoutineDao
import com.enricog.data.local.database.routines.dao.SegmentDao
import com.enricog.data.local.database.routines.model.InternalRoutine
import com.enricog.data.local.database.routines.model.InternalSegment
import com.enricog.data.local.database.timer.theme.dao.TimerThemeDao
import com.enricog.data.local.database.timer.theme.model.InternalTimerTheme
import kotlinx.serialization.json.Json

@Database(
    entities = [
        InternalRoutine::class,
        InternalSegment::class,
        InternalTimerTheme::class
    ],
    version = 4
)
@TypeConverters(
    TimeTypeConverter::class,
    OffsetDateTimeConverter::class,
    TimerThemeResourceConverter::class
)
internal abstract class TempoDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao

    abstract fun segmentDao(): SegmentDao

    abstract fun timerThemeDao(): TimerThemeDao

    companion object {
        private var INSTANCE: TempoDatabase? = null

        fun getInstance(context: Context, json: Json): TempoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, json)
            }
        }

        private fun buildDatabase(context: Context, json: Json): TempoDatabase {
            return Room
                .databaseBuilder(context.applicationContext, TempoDatabase::class.java, "Tempo.db")
                .createFromAsset("database/default.db")
                .addTypeConverter(TimerThemeResourceConverter(json = json))
                .addMigrations(MigrationFrom1To2)
                .addMigrations(MigrationFrom2To3)
                .addMigrations(MigrationFrom3To4)
                .build()
        }
    }
}
