package io.github.freewulkanowy.services.sync.notifications

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.Homework
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.pojos.GroupNotificationData
import io.github.freewulkanowy.data.pojos.NotificationData
import io.github.freewulkanowy.ui.modules.Destination
import io.github.freewulkanowy.ui.modules.splash.SplashActivity
import io.github.freewulkanowy.utils.getPlural
import io.github.freewulkanowy.utils.toFormattedString
import java.time.LocalDate
import javax.inject.Inject

class NewHomeworkNotification @Inject constructor(
    private val appNotificationManager: AppNotificationManager,
    @ApplicationContext private val context: Context
) {

    suspend fun notify(items: List<Homework>, student: Student) {
        val today = LocalDate.now()
        val lines = items.filter { !it.date.isBefore(today) }
            .map {
                "${it.date.toFormattedString("dd.MM")} - ${it.subject}: ${it.content}"
            }
            .ifEmpty { return }

        val notificationDataList = lines.map {
            NotificationData(
                title = context.getPlural(R.plurals.homework_notify_new_item_title, 1),
                content = it,
                destination = Destination.Homework,
            )
        }

        val groupNotificationData = GroupNotificationData(
            title = context.getPlural(R.plurals.homework_notify_new_item_title, lines.size),
            content = context.getPlural(
                R.plurals.homework_notify_new_item_content,
                lines.size,
                lines.size
            ),
            destination = Destination.Homework,
            type = NotificationType.NEW_HOMEWORK,
            notificationDataList = notificationDataList
        )

        appNotificationManager.sendMultipleNotifications(groupNotificationData, student)
    }
}
