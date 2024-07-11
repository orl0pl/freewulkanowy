package io.github.freewulkanowy.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.freewulkanowy.data.db.entities.SchoolAnnouncement
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
@Singleton
interface SchoolAnnouncementDao : BaseDao<SchoolAnnouncement> {

    @Query("SELECT * FROM SchoolAnnouncements WHERE user_login_id = :studentId ORDER BY date DESC")
    fun loadAll(studentId: Int): Flow<List<SchoolAnnouncement>>
}
