package io.github.freewulkanowy.ui.modules.grade

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.ui.base.BaseView

interface GradeView : BaseView {

    val currentPageIndex: Int

    fun initView()

    fun initTabs(pageCount: Int)

    fun showContent(show: Boolean)

    fun showProgress(show: Boolean)

    fun showErrorView(show: Boolean)

    fun setErrorDetails(message: String)

    fun showSemesterSwitch(show: Boolean)

    fun showSemesterDialog(selectedIndex: Int, semesters: List<Semester>)

    fun setCurrentSemesterName(semester: Int, schoolYear: Int)

    fun notifyChildLoadData(index: Int, semesterId: Int, forceRefresh: Boolean)

    fun notifyChildParentReselected(index: Int)

    fun notifyChildSemesterChange(index: Int)

    interface GradeChildView {

        fun onParentChangeSemester()

        fun onParentLoadData(semesterId: Int, forceRefresh: Boolean)

        fun onParentReselected()
    }
}
