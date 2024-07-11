package io.github.freewulkanowy.ui.modules.settings.appearance

import io.github.freewulkanowy.ui.base.BaseView

interface AppearanceView : BaseView {

    fun recreateView()

    fun updateLanguage(langCode: String)

    fun updateLanguageToFollowSystem()
}
