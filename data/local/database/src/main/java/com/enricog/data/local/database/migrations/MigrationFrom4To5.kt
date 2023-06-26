package com.enricog.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object MigrationFrom4To5: Migration(4, 5) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        try {
            database.execSQL("CREATE TABLE IF NOT EXISTS `RoutineStatistics` (`statisticId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `routineId_fk` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, `type` TEXT NOT NULL, `effectiveTime` INTEGER NOT NULL, FOREIGN KEY(`routineId_fk`) REFERENCES `Routines`(`routineId`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_RoutineStatistics_routineId_fk` ON `RoutineStatistics` (`routineId_fk`)")
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
    }
}