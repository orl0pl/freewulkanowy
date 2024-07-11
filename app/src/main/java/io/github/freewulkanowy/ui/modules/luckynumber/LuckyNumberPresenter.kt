package io.github.freewulkanowy.ui.modules.luckynumber

import io.github.freewulkanowy.data.*
import io.github.freewulkanowy.data.repositories.LuckyNumberRepository
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import io.github.freewulkanowy.utils.AnalyticsHelper
import timber.log.Timber
import javax.inject.Inject

class LuckyNumberPresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val luckyNumberRepository: LuckyNumberRepository,
    private val analytics: AnalyticsHelper
) : BasePresenter<LuckyNumberView>(errorHandler, studentRepository) {

    private lateinit var lastError: Throwable

    override fun onAttachView(view: LuckyNumberView) {
        super.onAttachView(view)
        view.run {
            initView()
            showContent(false)
            enableSwipe(false)
        }
        Timber.i("Lucky number view was initialized")
        errorHandler.showErrorMessage = ::showErrorViewOnError
        loadData()
    }

    private fun loadData(forceRefresh: Boolean = false) {
        flatResourceFlow {
            val student = studentRepository.getCurrentStudent()
            luckyNumberRepository.getLuckyNumber(student, forceRefresh)
        }
            .logResourceStatus("load lucky number")
            .onResourceData {
                if (it != null) {
                    view?.apply {
                        updateData(it)
                        showContent(true)
                        showEmpty(false)
                        showErrorView(false)
                    }
                } else {
                    view?.run {
                        showContent(false)
                        showEmpty(true)
                        showErrorView(false)
                    }
                }
            }
            .onResourceSuccess {
                if (it != null) {
                    analytics.logEvent(
                        "load_item",
                        "type" to "lucky_number",
                        "number" to it.luckyNumber
                    )
                }
            }
            .onResourceNotLoading {
                view?.run {
                    hideRefresh()
                    showProgress(false)
                    enableSwipe(true)
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

    fun onSwipeRefresh() {
        Timber.i("Force refreshing the lucky number")
        loadData(true)
    }

    fun onRetry() {
        view?.run {
            showErrorView(false)
            showProgress(true)
        }
        loadData(true)
    }

    fun onDetailsClick() {
        view?.showErrorDetailsDialog(lastError)
    }

    fun onViewReselected() {
        Timber.i("Luckynumber view is reselected")
        view?.popView()
    }
}
