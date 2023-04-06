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
internal class MigrationFrom3To4Test {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        TempoDatabase::class.java
    )

    @Test
    fun shouldApplyDatabaseMigration() {
        val db = helper.createDatabase(TEST_DB, 3)

        helper.runMigrationsAndValidate(TEST_DB, 4, true, MigrationFrom3To4)

        val cursor = db.query("SELECT * FROM TimerThemes")

        with(cursor) {
            moveToNext()
            assertThat(getString(getColumnIndex("name")))
                .isEqualTo("Default")
            assertThat(getString(getColumnIndex("description")))
                .isEqualTo("Default timer theme")
            assertThat(getString(getColumnIndex("preparationTimeResource")))
                .isEqualTo("{\"background\":{\"type\":\"color\",\"argb\":18419906090092199936},\"onBackground\":{\"type\":\"color\",\"argb\":18382036852342259712}}")
            assertThat(getString(getColumnIndex("stopwatchResource")))
                .isEqualTo("{\"background\":{\"type\":\"color\",\"argb\":18427765399207542784},\"onBackground\":{\"type\":\"color\",\"argb\":18382036852342259712}}")
            assertThat(getString(getColumnIndex("timerResource")))
                .isEqualTo("{\"background\":{\"type\":\"color\",\"argb\":18399386961144971264},\"onBackground\":{\"type\":\"color\",\"argb\":18382036852342259712}}")
            assertThat(getString(getColumnIndex("restResource")))
                .isEqualTo("{\"background\":{\"type\":\"color\",\"argb\":18446628135362363392},\"onBackground\":{\"type\":\"color\",\"argb\":18382036852342259712}}")
            assertThat(getInt(getColumnIndex("isLocked")))
                .isEqualTo(0)
            assertThat(getInt(getColumnIndex("isDefault")))
                .isEqualTo(1)
            assertThat(getString(getColumnIndex("createdAt")))
                .isEqualTo("2023-04-02T11:24:59.734679+02:00")
            assertThat(getString(getColumnIndex("updatedAt")))
                .isEqualTo("2023-04-02T11:24:59.734679+02:00")

            assertThat(cursor.isLast).isTrue()
        }
    }

    private companion object {
        private const val TEST_DB = "migration-test"
    }
}