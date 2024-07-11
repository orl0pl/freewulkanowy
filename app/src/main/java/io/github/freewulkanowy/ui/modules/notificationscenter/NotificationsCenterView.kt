package io.github.freewulkanowy.ui.modules.notificationscenter

import io.github.freewulkanowy.data.db.entities.Notification
import io.github.freewulkanowy.ui.base.BaseView

interface NotificationsCenterView : BaseView {

    val isViewEmpty: Boolean

    fun initView()

    fun updateData(data: List<Notification>)

    fun showProgress(show: Boolean)

    fun showEmpty(show: Boolean)

    fun showContent(show: Boolean)

    fun showErrorView(show: Boolean)

    fun setErrorDetails(message: String)
}
