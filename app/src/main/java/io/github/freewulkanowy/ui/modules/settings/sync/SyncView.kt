package io.github.freewulkanowy.ui.modules.settings.sync

import io.github.freewulkanowy.ui.base.BaseView

interface SyncView : BaseView {

    val syncSuccessString: String

    val syncFailedString: String

    fun initView()

    fun setLastSyncDate(lastSyncDate: String)

    fun setServicesSuspended(serviceEnablesKey: String, isHolidays: Boolean)

    fun setSyncInProgress(inProgress: Boolean)
}
