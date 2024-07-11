package io.github.freewulkanowy.services.sync.notifications

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.Message
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.pojos.GroupNotificationData
import io.github.freewulkanowy.data.pojos.NotificationData
import io.github.freewulkanowy.ui.modules.Destination
import io.github.freewulkanowy.utils.getPlural
import javax.inject.Inject

class NewMessageNotification @Inject constructor(
    private val appNotificationManager: AppNotificationManager,
    @ApplicationContext private val context: Context
) {

    suspend fun notify(items: List<Message>, student: Student) {
        val notificationDataList = items.map {
            NotificationData(
                title = context.getPlural(R.plurals.message_new_items, 1),
                content = "${it.correspondents}: ${it.subject}",
                destination = Destination.Message,
            )
        }

        val groupNotificationData = GroupNotificationData(
            notificationDataList = notificationDataList,
            title = context.getPlural(R.plurals.message_new_items, items.size),
            content = context.getPlural(R.plurals.message_notify_new_items, items.size, items.size),
            destination = Destination.Message,
            type = NotificationType.NEW_MESSAGE
        )

        appNotificationManager.sendMultipleNotifications(groupNotificationData, student)
    }
}
