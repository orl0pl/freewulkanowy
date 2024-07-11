package io.github.freewulkanowy.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.freewulkanowy.data.db.SharedPrefProvider
import io.github.freewulkanowy.data.enums.GradeExpandMode

class Migration41(private val sharedPrefProvider: SharedPrefProvider) : Migration(40, 41) {

    override fun migrate(db: SupportSQLiteDatabase) {
        migrateSharedPreferences()
        db.execSQL("ALTER TABLE Homework ADD COLUMN is_added_by_user INTEGER NOT NULL DEFAULT 0")
    }

    private fun migrateSharedPreferences() {
        if (sharedPrefProvider.getBoolean("pref_key_expand_grade", false)) {
            sharedPrefProvider.putString("pref_key_expand_grade_mode", GradeExpandMode.ALWAYS_EXPANDED.value)
        }
        sharedPrefProvider.delete("pref_key_expand_grade")
    }
}
