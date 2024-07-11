package io.github.freewulkanowy.ui.modules.debug.notification

import io.github.freewulkanowy.ui.base.BaseView

interface NotificationDebugView : BaseView {

    fun initView()

    fun setItems(notificationDebugs: List<NotificationDebugItem>)
}
