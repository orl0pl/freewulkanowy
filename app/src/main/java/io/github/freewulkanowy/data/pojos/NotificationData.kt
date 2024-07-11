package io.github.freewulkanowy.data.pojos

import io.github.freewulkanowy.services.sync.notifications.NotificationType
import io.github.freewulkanowy.ui.modules.Destination

data class NotificationData(
    val destination: Destination,
    val title: String,
    val content: String
)

data class GroupNotificationData(
    val notificationDataList: List<NotificationData>,
    val title: String,
    val content: String,
    val destination: Destination,
    val type: NotificationType
)

