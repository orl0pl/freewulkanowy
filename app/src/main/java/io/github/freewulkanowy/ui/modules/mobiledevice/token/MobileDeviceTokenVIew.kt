package io.github.freewulkanowy.ui.modules.mobiledevice.token

import io.github.freewulkanowy.data.pojos.MobileDeviceToken
import io.github.freewulkanowy.ui.base.BaseView

interface MobileDeviceTokenVIew : BaseView {

    fun initView()

    fun hideLoading()

    fun showContent()

    fun closeDialog()

    fun updateData(token: MobileDeviceToken)
}
