package com.enricog.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object MigrationFrom2To3 : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        try {
            database.execSQL("CREATE TABLE RoutinesTemp (routineId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, preparationTimeInSeconds INTEGER NOT NULL, createdAt TEXT NOT NULL, updatedAt TEXT NOT NULL, rank TEXT NOT NULL)")

            database.execSQL("INSERT INTO RoutinesTemp(routineId, name, preparationTimeInSeconds, createdAt, updatedAt, rank) SELECT routineId, name, startTimeOffsetInSeconds, createdAt, updatedAt, rank FROM Routines")

            database.execSQL("DROP TABLE Routines")

            database.execSQL("ALTER TABLE RoutinesTemp RENAME TO Routines")

            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
    }
}