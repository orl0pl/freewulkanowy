package io.github.freewulkanowy.ui.modules.more

import io.github.freewulkanowy.ui.base.BaseView
import io.github.freewulkanowy.ui.modules.Destination

interface MoreView : BaseView {

    fun initView()

    fun updateData(data: List<MoreItem>)

    fun popView(depth: Int)

    fun openView(destination: Destination)
}
