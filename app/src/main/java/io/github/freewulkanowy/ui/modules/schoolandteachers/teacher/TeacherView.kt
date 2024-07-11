package io.github.freewulkanowy.ui.modules.schoolandteachers.teacher

import io.github.freewulkanowy.data.db.entities.Teacher
import io.github.freewulkanowy.ui.base.BaseView
import io.github.freewulkanowy.ui.modules.schoolandteachers.SchoolAndTeachersChildView

interface TeacherView : BaseView, SchoolAndTeachersChildView {

    val isViewEmpty: Boolean

    val noSubjectString: String

    fun initView()

    fun updateData(data: List<Teacher>)

    fun hideRefresh()

    fun showProgress(show: Boolean)

    fun enableSwipe(enable: Boolean)

    fun showContent(show: Boolean)

    fun showEmpty(show: Boolean)

    fun showErrorView(show: Boolean)

    fun setErrorDetails(message: String)
}
