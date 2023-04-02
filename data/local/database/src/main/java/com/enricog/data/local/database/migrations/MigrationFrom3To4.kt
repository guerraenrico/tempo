package com.enricog.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object MigrationFrom3To4 : Migration(3, 4) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        try {
            database.execSQL("CREATE TABLE IF NOT EXISTS TimerThemes (timerThemeId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, description TEXT NOT NULL, preparationTimeResource TEXT NOT NULL, stopwatchResource TEXT NOT NULL, timerResource TEXT NOT NULL, restResource TEXT NOT NULL, isLocked INTEGER NOT NULL, isDefault INTEGER NOT NULL, createdAt TEXT NOT NULL, updatedAt TEXT NOT NULL)")

            database.execSQL("INSERT INTO TimerThemes (`name`, `description`, `preparationTimeResource`, `stopwatchResource`, `timerResource`, `restResource`, `isLocked`, `isDefault`, `createdAt`, `updatedAt`) VALUES ('Default', 'Default timer theme', '{\"background\":{\"type\":\"color\",\"argb\":18419906090092199936},\"onBackground\":{\"type\":\"color\",\"argb\":18446744069414584320}}', '{\"background\":{\"type\":\"color\",\"argb\":18427765399207542784},\"onBackground\":{\"type\":\"color\",\"argb\":18446744069414584320}}', '{\"background\":{\"type\":\"color\",\"argb\":18399386961144971264},\"onBackground\":{\"type\":\"color\",\"argb\":18446744069414584320}}', '{\"background\":{\"type\":\"color\",\"argb\":18446628135362363392},\"onBackground\":{\"type\":\"color\",\"argb\":18446744069414584320}}', 0, 1, '2023-04-02T11:24:59.734679+02:00', '2023-04-02T11:24:59.734679+02:00')")

            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
    }
}