package io.github.freewulkanowy.ui.modules.splash

import io.github.freewulkanowy.ui.base.BaseView
import io.github.freewulkanowy.ui.modules.Destination

interface SplashView : BaseView {

    fun openLoginView()

    fun openMainView(destination: Destination?)

    fun openExternalUrlAndFinish(url: String)
}
