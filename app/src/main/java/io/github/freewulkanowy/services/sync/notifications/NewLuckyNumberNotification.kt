package io.github.freewulkanowy.services.sync.notifications

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.freewulkanowy.R
import io.github.freewulkanowy.data.db.entities.LuckyNumber
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.pojos.NotificationData
import io.github.freewulkanowy.ui.modules.Destination
import io.github.freewulkanowy.ui.modules.splash.SplashActivity
import javax.inject.Inject

class NewLuckyNumberNotification @Inject constructor(
    private val appNotificationManager: AppNotificationManager,
    @ApplicationContext private val context: Context
) {

    suspend fun notify(item: LuckyNumber, student: Student) {
        val notificationData = NotificationData(
            title = context.getString(R.string.lucky_number_notify_new_item_title),
            content = context.getString(
                R.string.lucky_number_notify_new_item,
                item.luckyNumber.toString()
            ),
            destination = Destination.LuckyNumber
        )

        appNotificationManager.sendSingleNotification(
            notificationData = notificationData,
            notificationType = NotificationType.NEW_LUCKY_NUMBER,
            student = student
        )
    }
}
