package io.github.freewulkanowy.ui.modules.debug

import io.github.freewulkanowy.ui.base.BaseView

interface DebugView : BaseView {

    fun initView()

    fun setItems(itemList: List<DebugItem>)

    fun openLogViewer()

    fun openNotificationsDebug()

    fun clearWebkitCookies()
}
