package io.github.freewulkanowy.ui.modules.attendance.calculator

import io.github.freewulkanowy.data.flatResourceFlow
import io.github.freewulkanowy.data.logResourceStatus
import io.github.freewulkanowy.data.onResourceData
import io.github.freewulkanowy.data.onResourceError
import io.github.freewulkanowy.data.onResourceIntermediate
import io.github.freewulkanowy.data.onResourceNotLoading
import io.github.freewulkanowy.data.repositories.SemesterRepository
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.domain.attendance.GetAttendanceCalculatorDataUseCase
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import timber.log.Timber
import javax.inject.Inject

class AttendanceCalculatorPresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val semesterRepository: SemesterRepository,
    private val getAttendanceCalculatorData: GetAttendanceCalculatorDataUseCase,
) : BasePresenter<AttendanceCalculatorView>(errorHandler, studentRepository) {

    private lateinit var lastError: Throwable

    override fun onAttachView(view: AttendanceCalculatorView) {
        super.onAttachView(view)
        view.initView()
        Timber.i("Attendance calculator view was initialized")
        errorHandler.showErrorMessage = ::showErrorViewOnError
        loadData()
    }

    fun onSwipeRefresh() {
        Timber.i("Force refreshing the attendance calculator")
        loadData(forceRefresh = true)
    }

    fun onRetry() {
        view?.run {
            showErrorView(false)
            showProgress(true)
        }
        loadData()
    }

    fun onDetailsClick() {
        view?.showErrorDetailsDialog(lastError)
    }

    private fun loadData(forceRefresh: Boolean = false) {
        flatResourceFlow {
            val student = studentRepository.getCurrentStudent()
            val semester = semesterRepository.getCurrentSemester(student)
            getAttendanceCalculatorData(student, semester, forceRefresh)
        }
            .logResourceStatus("load attendance calculator")
            .onResourceData {
                view?.run {
                    showProgress(false)
                    showErrorView(false)
                    showContent(it.isNotEmpty())
                    showEmpty(it.isEmpty())
                    updateData(it)
                }
            }
            .onResourceIntermediate { view?.showRefresh(true) }
            .onResourceNotLoading {
                view?.run {
                    enableSwipe(true)
                    showRefresh(false)
                    showProgress(false)
                }
            }
            .onResourceError(errorHandler::dispatch)
            .launch()
    }

    private fun showErrorViewOnError(message: String, error: Throwable) {
        view?.run {
            if (isViewEmpty) {
                lastError = error
                setErrorDetails(message)
                showErrorView(true)
                showEmpty(false)
            } else showError(message, error)
        }
    }

    fun onSettingsSelected(): Boolean {
        view?.openSettingsView()
        return true
    }
}
