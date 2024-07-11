package io.github.freewulkanowy.ui.modules.homework.details

import io.github.freewulkanowy.ui.base.BaseView

interface HomeworkDetailsView : BaseView {

    val homeworkDeleteSuccess: String

    fun initView()

    fun closeDialog()

    fun updateMarkAsDoneLabel(isDone: Boolean)
}
