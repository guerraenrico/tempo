package com.enricog.data.local.database.migrations

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.enricog.data.local.database.TempoDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class MigrationFrom4To5Test {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        TempoDatabase::class.java
    )

    @Test
    fun shouldApplyDatabaseMigration() {
        helper.createDatabase(TEST_DB, 4)
        helper.runMigrationsAndValidate(TEST_DB, 5, true, MigrationFrom4To5)
    }


    private companion object {
        private const val TEST_DB = "migration-test"
    }
}