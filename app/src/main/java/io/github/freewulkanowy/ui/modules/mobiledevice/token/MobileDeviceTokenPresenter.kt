package io.github.freewulkanowy.ui.modules.mobiledevice.token

import io.github.freewulkanowy.data.*
import io.github.freewulkanowy.data.repositories.MobileDeviceRepository
import io.github.freewulkanowy.data.repositories.SemesterRepository
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import io.github.freewulkanowy.utils.AnalyticsHelper
import timber.log.Timber
import javax.inject.Inject

class MobileDeviceTokenPresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val semesterRepository: SemesterRepository,
    private val mobileDeviceRepository: MobileDeviceRepository,
    private val analytics: AnalyticsHelper
) : BasePresenter<MobileDeviceTokenVIew>(errorHandler, studentRepository) {

    override fun onAttachView(view: MobileDeviceTokenVIew) {
        super.onAttachView(view)
        view.initView()
        Timber.i("Mobile device view was initialized")
        loadData()
    }

    private fun loadData() {
        resourceFlow {
            val student = studentRepository.getCurrentStudent()
            val semester = semesterRepository.getCurrentSemester(student)
            mobileDeviceRepository.getToken(student, semester)
        }
            .logResourceStatus("load mobile device registration")
            .onResourceData {
                view?.run {
                    updateData(it)
                    showContent()
                }
            }
            .onResourceSuccess {
                analytics.logEvent(
                    "device_register",
                    "symbol" to it.token.substring(0, 3)
                )
            }
            .onResourceNotLoading { view?.hideLoading() }
            .onResourceError {
                view?.closeDialog()
                errorHandler.dispatch(it)
            }
            .launch()
    }
}
