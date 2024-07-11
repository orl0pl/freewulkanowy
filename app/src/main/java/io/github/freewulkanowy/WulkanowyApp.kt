package io.github.freewulkanowy

import android.app.Application
import android.util.Log.DEBUG
import android.util.Log.INFO
import android.util.Log.VERBOSE
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import fr.bipi.treessence.file.FileLoggerTree
import io.github.freewulkanowy.data.repositories.PreferencesRepository
import io.github.freewulkanowy.ui.base.ThemeManager
import io.github.freewulkanowy.utils.ActivityLifecycleLogger
import io.github.freewulkanowy.utils.AnalyticsHelper
import io.github.freewulkanowy.utils.AppInfo
import io.github.freewulkanowy.utils.CrashLogExceptionTree
import io.github.freewulkanowy.utils.CrashLogTree
import io.github.freewulkanowy.utils.DebugLogTree
import io.github.freewulkanowy.utils.RemoteConfigHelper
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class WulkanowyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var appInfo: AppInfo

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var remoteConfigHelper: RemoteConfigHelper

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(if (appInfo.isDebug) VERBOSE else INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        initializeAppLanguage()
        themeManager.applyDefaultTheme()
        remoteConfigHelper.initialize()
        initLogging()
    }

    private fun initLogging() {
        if (appInfo.isDebug) {
            Timber.plant(DebugLogTree())
            Timber.plant(
                FileLoggerTree.Builder()
                    .withFileName("wulkanowy.%g.log")
                    .withDirName(applicationContext.filesDir.absolutePath)
                    .withFileLimit(10)
                    .withMinPriority(DEBUG)
                    .build()
            )
        } else {
            Timber.plant(CrashLogExceptionTree())
            Timber.plant(CrashLogTree())
        }
        registerActivityLifecycleCallbacks(ActivityLifecycleLogger())
    }

    private fun initializeAppLanguage() {
        Lingver.init(this)

        if (preferencesRepository.appLanguage == "system") {
            Lingver.getInstance().setFollowSystemLocale(this)
            analyticsHelper.logEvent("language", "startup" to appInfo.systemLanguage)
        } else {
            analyticsHelper.logEvent("language", "startup" to preferencesRepository.appLanguage)
        }
    }
}
