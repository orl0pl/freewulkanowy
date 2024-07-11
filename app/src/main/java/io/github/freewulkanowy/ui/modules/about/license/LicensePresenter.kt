package io.github.freewulkanowy.ui.modules.about.license

import com.mikepenz.aboutlibraries.entity.Library
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import io.github.freewulkanowy.utils.DispatchersProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LicensePresenter @Inject constructor(
    private val dispatchers: DispatchersProvider,
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository
) : BasePresenter<LicenseView>(errorHandler, studentRepository) {

    override fun onAttachView(view: LicenseView) {
        super.onAttachView(view)
        view.initView()
        loadData()
    }

    fun onItemSelected(library: Library) {
        view?.run { library.licenses.firstOrNull()?.licenseContent?.let { openLicense(it) } }
    }

    private fun loadData() {
        presenterScope.launch {
            runCatching {
                withContext(dispatchers.io) {
                    view?.appLibraries.orEmpty()
                }
            }
                .onFailure { errorHandler.dispatch(it) }
                .onSuccess { view?.updateData(it) }

            view?.showProgress(false)
        }
    }
}
