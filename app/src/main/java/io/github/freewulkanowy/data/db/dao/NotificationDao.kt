package io.github.freewulkanowy.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.freewulkanowy.data.db.entities.Notification
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
@Dao
interface NotificationDao : BaseDao<Notification> {

    @Query("SELECT * FROM Notifications WHERE student_id = :studentId OR student_id = -1")
    fun loadAll(studentId: Long): Flow<List<Notification>>
}
