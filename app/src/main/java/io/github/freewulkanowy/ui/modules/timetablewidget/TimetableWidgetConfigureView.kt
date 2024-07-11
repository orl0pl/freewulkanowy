package io.github.freewulkanowy.ui.modules.timetablewidget

import io.github.freewulkanowy.data.db.entities.StudentWithSemesters
import io.github.freewulkanowy.ui.base.BaseView

interface TimetableWidgetConfigureView : BaseView {

    fun initView()

    fun updateData(data: List<StudentWithSemesters>, selectedStudentId: Long)

    fun updateTimetableWidget(widgetId: Int)

    fun setSuccessResult(widgetId: Int)

    fun finishView()

    fun openLoginView()
}
