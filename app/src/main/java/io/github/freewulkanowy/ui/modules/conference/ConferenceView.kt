package io.github.freewulkanowy.ui.modules.conference

import io.github.freewulkanowy.data.db.entities.Conference
import io.github.freewulkanowy.ui.base.BaseView

interface ConferenceView : BaseView {

    val isViewEmpty: Boolean

    fun initView()

    fun updateData(data: List<Conference>)

    fun clearData()

    fun showRefresh(show: Boolean)

    fun showEmpty(show: Boolean)

    fun showErrorView(show: Boolean)

    fun setErrorDetails(message: String)

    fun showProgress(show: Boolean)

    fun enableSwipe(enable: Boolean)

    fun showContent(show: Boolean)

    fun openConferenceDialog(conference: Conference)

    fun resetView()
}
