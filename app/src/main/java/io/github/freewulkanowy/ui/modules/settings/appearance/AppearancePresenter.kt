package io.github.freewulkanowy.ui.modules.settings.appearance

import io.github.freewulkanowy.data.repositories.PreferencesRepository
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import io.github.freewulkanowy.utils.AnalyticsHelper
import io.github.freewulkanowy.utils.AppInfo
import timber.log.Timber
import javax.inject.Inject

class AppearancePresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val preferencesRepository: PreferencesRepository,
    private val analytics: AnalyticsHelper,
    private val appInfo: AppInfo
) : BasePresenter<AppearanceView>(errorHandler, studentRepository) {

    override fun onAttachView(view: AppearanceView) {
        super.onAttachView(view)
        Timber.i("Settings appearance view was initialized")
    }

    fun onSharedPreferenceChanged(key: String?) {
        key ?: return
        Timber.i("Change settings $key")

        preferencesRepository.apply {
            when (key) {
                appThemeKey -> view?.recreateView()
                appLanguageKey -> view?.run {
                    if (appLanguage == "system") {
                        updateLanguageToFollowSystem()
                        analytics.logEvent("language", "setting_changed" to appInfo.systemLanguage)
                    } else {
                        updateLanguage(appLanguage)
                        analytics.logEvent("language", "setting_changed" to appLanguage)
                    }
                    recreateView()
                }
            }
        }
        analytics.logEvent("setting_changed", "name" to key)
    }
}
