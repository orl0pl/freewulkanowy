package io.github.freewulkanowy.ui.modules.login

import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.data.repositories.WulkanowyRepository
import io.github.freewulkanowy.services.sync.SyncManager
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val wulkanowyRepository: WulkanowyRepository,
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val syncManager: SyncManager
) : BasePresenter<LoginView>(errorHandler, studentRepository) {

    override fun onAttachView(view: LoginView) {
        super.onAttachView(view)
        view.initView()
        Timber.i("Login view was initialized")
    }

    fun updateSdkMappings() {
        presenterScope.launch {
            runCatching { wulkanowyRepository.fetchMapping() }
                .onFailure { Timber.e(it) }
        }
    }
}
