package io.github.freewulkanowy.ui.modules.luckynumberwidget

import io.github.freewulkanowy.data.db.entities.StudentWithSemesters
import io.github.freewulkanowy.ui.base.BaseView

interface LuckyNumberWidgetConfigureView : BaseView {

    fun initView()

    fun updateData(data: List<StudentWithSemesters>, selectedStudentId: Long)

    fun updateLuckyNumberWidget(widgetId: Int)

    fun setSuccessResult(widgetId: Int)

    fun finishView()

    fun openLoginView()
}
