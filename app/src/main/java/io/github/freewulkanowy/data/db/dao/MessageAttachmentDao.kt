package io.github.freewulkanowy.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.github.freewulkanowy.data.db.entities.MessageAttachment

@Dao
interface MessageAttachmentDao : BaseDao<MessageAttachment> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(items: List<MessageAttachment>): List<Long>
}
