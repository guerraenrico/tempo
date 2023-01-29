package com.enricog.data.local.database.migrations

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.enricog.data.local.database.TempoDatabase
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationFrom1To2Test {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        TempoDatabase::class.java
    )

    @Test
    fun shouldApplyRankWhenDatabaseContainsRoutines() {
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL(
                """
                INSERT INTO Routines (name, startTimeOffsetInSeconds, createdAt, updatedAt)
                VALUES ('Routine 1', 0, '2022-12-25T17:49:30+01:00', '2022-12-25T17:49:30+01:00')
            """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO Routines (name, startTimeOffsetInSeconds, createdAt, updatedAt)
                VALUES ('Routine 2', 0, '2022-12-25T17:50:30+01:00', '2022-12-25T17:50:30+01:00')
            """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO Routines (name, startTimeOffsetInSeconds, createdAt, updatedAt)
                VALUES ('Routine 3', 0, '2022-12-25T17:51:30+01:00', '2022-12-25T17:51:30+01:00')
            """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO Routines (name, startTimeOffsetInSeconds, createdAt, updatedAt)
                VALUES ('Routine 4', 0, '2022-12-25T17:52:30+01:00', '2022-12-25T17:52:30+01:00')
            """.trimIndent()
            )

            close()
        }

        val db = helper.runMigrationsAndValidate(TEST_DB, 2, true, MigrationFrom1To2)

        val cursor = db.query("SELECT * FROM Routines ORDER BY createdAt DESC")

        with(cursor) {
            moveToNext()
            assertThat(getString(getColumnIndex("name"))).isEqualTo("Routine 4")
            assertThat(getString(getColumnIndex("rank"))).isEqualTo("mzzzzz")

            moveToNext()
            assertThat(getString(getColumnIndex("name"))).isEqualTo("Routine 3")
            assertThat(getString(getColumnIndex("rank"))).isEqualTo("tmzzzz")

            moveToNext()
            assertThat(getString(getColumnIndex("name"))).isEqualTo("Routine 2")
            assertThat(getString(getColumnIndex("rank"))).isEqualTo("wtmzzz")

            moveToNext()
            assertThat(getString(getColumnIndex("name"))).isEqualTo("Routine 1")
            assertThat(getString(getColumnIndex("rank"))).isEqualTo("yjtmzz")
        }
    }

    @Test
    fun shouldApplyOnlyMigrationWhenDatabaseHasNoRoutines() {
        helper.createDatabase(TEST_DB, 1).apply { close() }

        helper.runMigrationsAndValidate(TEST_DB, 2, true, MigrationFrom1To2)
    }

    private companion object {
        private const val TEST_DB = "migration-test"
    }
}