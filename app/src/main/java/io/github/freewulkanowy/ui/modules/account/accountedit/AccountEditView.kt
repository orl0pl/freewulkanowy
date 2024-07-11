package io.github.freewulkanowy.ui.modules.account.accountedit

import io.github.freewulkanowy.ui.base.BaseView

interface AccountEditView : BaseView {

    fun initView()

    fun popView()

    fun recreateMainView()

    fun showCurrentNick(nick: String)

    fun updateSelectedColorData(color: Int)

    fun updateColorsData(colors: List<Int>)
}
