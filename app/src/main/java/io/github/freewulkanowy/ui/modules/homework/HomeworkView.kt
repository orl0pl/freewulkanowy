package io.github.freewulkanowy.ui.modules.homework

import io.github.freewulkanowy.data.db.entities.Homework
import io.github.freewulkanowy.ui.base.BaseView

interface HomeworkView : BaseView {

    val isViewEmpty: Boolean

    fun initView()

    fun updateData(data: List<HomeworkItem<*>>)

    fun clearData()

    fun updateNavigationWeek(date: String)

    fun showRefresh(show: Boolean)

    fun showEmpty(show: Boolean)

    fun showErrorView(show: Boolean)

    fun setErrorDetails(message: String)

    fun showProgress(show: Boolean)

    fun enableSwipe(enable: Boolean)

    fun showContent(show: Boolean)

    fun showPreButton(show: Boolean)

    fun showNextButton(show: Boolean)

    fun showHomeworkDialog(homework: Homework)

    fun showAddHomeworkDialog()

    fun resetView()
}
