package io.github.freewulkanowy.ui.modules.exam

import io.github.freewulkanowy.data.db.entities.Exam
import io.github.freewulkanowy.ui.base.BaseView

interface ExamView : BaseView {

    val isViewEmpty: Boolean

    fun initView()

    fun updateData(data: List<ExamItem<*>>)

    fun updateNavigationWeek(date: String)

    fun clearData()

    fun showRefresh(show: Boolean)

    fun showEmpty(show: Boolean)

    fun showErrorView(show: Boolean)

    fun setErrorDetails(message: String)

    fun showProgress(show: Boolean)

    fun enableSwipe(enable: Boolean)

    fun showContent(show: Boolean)

    fun showNextButton(show: Boolean)

    fun showPreButton(show: Boolean)

    fun showExamDialog(exam: Exam)

    fun resetView()
}
