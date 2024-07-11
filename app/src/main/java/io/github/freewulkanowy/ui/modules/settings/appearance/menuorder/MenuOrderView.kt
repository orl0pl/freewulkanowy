package io.github.freewulkanowy.ui.modules.settings.appearance.menuorder

import io.github.freewulkanowy.ui.base.BaseView

interface MenuOrderView : BaseView {

    fun initView()

    fun updateData(data: List<MenuOrderItem>)

    fun restartApp()

    fun showRestartConfirmationDialog()

    fun popView()
}
