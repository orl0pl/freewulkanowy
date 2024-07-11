package io.github.freewulkanowy.ui.modules.homework.add

import io.github.freewulkanowy.ui.base.BaseView
import java.time.LocalDate

interface HomeworkAddView : BaseView {

    fun initView()

    fun showSuccessMessage()

    fun setErrorSubjectRequired()

    fun setErrorDateRequired()

    fun setErrorContentRequired()

    fun closeDialog()

    fun showDatePickerDialog(selectedDate: LocalDate)
}
