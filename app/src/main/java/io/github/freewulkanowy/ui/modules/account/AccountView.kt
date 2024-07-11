package io.github.freewulkanowy.ui.modules.account

import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.ui.base.BaseView

interface AccountView : BaseView {

    fun initView()

    fun updateData(data: List<AccountItem<*>>)

    fun openLoginView()

    fun openAccountDetailsView(student: Student)
}
