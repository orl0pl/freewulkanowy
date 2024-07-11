package io.github.freewulkanowy.ui.modules.account.accountquick

import io.github.freewulkanowy.ui.base.BaseView
import io.github.freewulkanowy.ui.modules.account.AccountItem

interface AccountQuickView : BaseView {

    fun initView()

    fun updateData(data: List<AccountItem<*>>)

    fun recreateMainView()

    fun popView()

    fun openAccountView()
}
