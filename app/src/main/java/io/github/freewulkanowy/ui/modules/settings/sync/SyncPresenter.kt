package io.github.freewulkanowy.ui.modules.settings.sync

import androidx.work.WorkInfo
import io.github.freewulkanowy.data.repositories.PreferencesRepository
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.services.sync.SyncManager
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import io.github.freewulkanowy.utils.AnalyticsHelper
import io.github.freewulkanowy.utils.isHolidays
import io.github.freewulkanowy.utils.toFormattedString
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.time.LocalDate.now
import javax.inject.Inject

class SyncPresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val preferencesRepository: PreferencesRepository,
    private val analytics: AnalyticsHelper,
    private val syncManager: SyncManager,
) : BasePresenter<SyncView>(errorHandler, studentRepository) {

    override fun onAttachView(view: SyncView) {
        super.onAttachView(view)
        Timber.i("Settings sync view was initialized")
        view.setServicesSuspended(preferencesRepository.serviceEnableKey, now().isHolidays)
        view.initView()
        setSyncDateInView()
    }

    fun onSharedPreferenceChanged(key: String?) {
        key ?: return
        Timber.i("Change settings $key")

        preferencesRepository.apply {
            when (key) {
                serviceEnableKey -> with(syncManager) { if (isServiceEnabled) startPeriodicSyncWorker() else stopSyncWorker() }
                servicesIntervalKey, servicesOnlyWifiKey -> syncManager.startPeriodicSyncWorker(true)
            }
        }
        analytics.logEvent("setting_changed", "name" to key)
    }

    fun onSyncNowClicked() {
        view?.run {
            syncManager.startOneTimeSyncWorker().onEach { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.ENQUEUED -> {
                        setSyncInProgress(true)
                        Timber.i("Setting sync now started")
                        analytics.logEvent("sync_now", "status" to "started")
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        showMessage(syncSuccessString)
                        analytics.logEvent("sync_now", "status" to "success")
                    }

                    WorkInfo.State.FAILED -> {
                        showError(
                            syncFailedString,
                            Throwable(
                                message = workInfo.outputData.getString("error_message"),
                                cause = Throwable(workInfo.outputData.getString("error_stack"))
                            )
                        )
                        analytics.logEvent("sync_now", "status" to "failed")
                    }

                    else -> Timber.d("Sync now state: ${workInfo?.state}")
                }
                if (workInfo?.state?.isFinished == true) {
                    setSyncInProgress(false)
                    setSyncDateInView()
                }
            }.catch {
                Timber.e(it, "Sync now failed")
            }.launch("sync")
        }
    }

    private fun setSyncDateInView() {
        val lastSyncDate = preferencesRepository.lasSyncDate ?: return

        view?.setLastSyncDate(lastSyncDate.toFormattedString("dd.MM.yyyy HH:mm:ss"))
    }
}
