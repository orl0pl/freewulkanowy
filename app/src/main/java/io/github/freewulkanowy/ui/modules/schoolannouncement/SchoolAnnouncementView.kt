package io.github.freewulkanowy.ui.modules.schoolannouncement

import io.github.freewulkanowy.data.db.entities.SchoolAnnouncement
import io.github.freewulkanowy.ui.base.BaseView

interface SchoolAnnouncementView : BaseView {

    val isViewEmpty: Boolean

    fun initView()

    fun updateData(data: List<SchoolAnnouncement>)

    fun clearData()

    fun showEmpty(show: Boolean)

    fun showErrorView(show: Boolean)

    fun setErrorDetails(message: String)

    fun openSchoolAnnouncementDialog(item: SchoolAnnouncement)

    fun showProgress(show: Boolean)

    fun enableSwipe(enable: Boolean)

    fun showContent(show: Boolean)

    fun showRefresh(show: Boolean)
}
