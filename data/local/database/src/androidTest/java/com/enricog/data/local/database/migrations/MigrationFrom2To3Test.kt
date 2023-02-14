package com.enricog.data.local.database.migrations

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.enricog.data.local.database.TempoDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationFrom2To3Test {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        TempoDatabase::class.java
    )

    @Test
    fun shouldApplyDatabaseMigration() {
        helper.createDatabase(TEST_DB, 2).apply {
            execSQL(
                """
                INSERT INTO Routines (name, startTimeOffsetInSeconds, createdAt, updatedAt, rank)
                VALUES ('Routine 1', 0, '2022-12-25T17:49:30+01:00', '2022-12-25T17:49:30+01:00', 'bbbbbb')
            """.trimIndent()
            )
            close()
        }

        helper.runMigrationsAndValidate(TEST_DB, 3, true, MigrationFrom2To3)
    }

    private companion object {
        private const val TEST_DB = "migration-test"
    }
}