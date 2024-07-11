package io.github.freewulkanowy.ui.modules.message

import androidx.annotation.StringRes
import io.github.freewulkanowy.ui.base.BaseView

interface MessageView : BaseView {

    val currentPageIndex: Int

    fun initView()

    fun showContent(show: Boolean)

    fun showProgress(show: Boolean)

    fun showMessage(@StringRes messageId: Int)

    fun showNewMessage(show: Boolean)

    fun showTabLayout(show: Boolean)

    fun notifyChildLoadData(index: Int, forceRefresh: Boolean)

    fun notifyChildrenFinishActionMode()

    fun notifyChildParentReselected(index: Int)

    fun openSendMessage()

    fun popView()
}
