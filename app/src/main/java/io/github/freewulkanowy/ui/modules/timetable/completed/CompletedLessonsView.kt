package io.github.freewulkanowy.ui.modules.timetable.completed

import io.github.freewulkanowy.data.db.entities.CompletedLesson
import io.github.freewulkanowy.ui.base.BaseView
import java.time.LocalDate

interface CompletedLessonsView : BaseView {

    val isViewEmpty: Boolean

    fun initView()

    fun updateData(data: List<CompletedLesson>)

    fun clearData()

    fun updateNavigationDay(date: String)

    fun showRefresh(show: Boolean)

    fun showEmpty(show: Boolean)

    fun showErrorView(show: Boolean)

    fun setErrorDetails(message: String)

    fun showFeatureDisabled()

    fun showProgress(show: Boolean)

    fun enableSwipe(enable: Boolean)

    fun showContent(show: Boolean)

    fun showPreButton(show: Boolean)

    fun showNextButton(show: Boolean)

    fun showCompletedLessonDialog(completedLesson: CompletedLesson)

    fun showDatePickerDialog(selectedDate: LocalDate)
}
