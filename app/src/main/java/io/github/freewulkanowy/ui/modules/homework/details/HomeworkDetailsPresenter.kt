package io.github.freewulkanowy.ui.modules.homework.details

import io.github.freewulkanowy.data.db.entities.Homework
import io.github.freewulkanowy.data.logResourceStatus
import io.github.freewulkanowy.data.onResourceError
import io.github.freewulkanowy.data.onResourceSuccess
import io.github.freewulkanowy.data.repositories.HomeworkRepository
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.data.resourceFlow
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import io.github.freewulkanowy.utils.AnalyticsHelper
import timber.log.Timber
import javax.inject.Inject

class HomeworkDetailsPresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val homeworkRepository: HomeworkRepository,
    private val analytics: AnalyticsHelper,
) : BasePresenter<HomeworkDetailsView>(errorHandler, studentRepository) {

    override fun onAttachView(view: HomeworkDetailsView) {
        super.onAttachView(view)
        view.initView()
        Timber.i("Homework details view was initialized")
    }

    fun deleteHomework(homework: Homework) {
        resourceFlow { homeworkRepository.deleteHomework(homework) }
            .logResourceStatus("homework delete")
            .onResourceSuccess {
                view?.run {
                    showMessage(homeworkDeleteSuccess)
                    closeDialog()
                }
            }
            .onResourceError(errorHandler::dispatch)
            .launch("delete")
    }

    fun toggleDone(homework: Homework) {
        resourceFlow { homeworkRepository.toggleDone(homework) }
            .logResourceStatus("homework details update")
            .onResourceSuccess {
                view?.updateMarkAsDoneLabel(homework.isDone)
                analytics.logEvent("homework_mark_as_done")
            }
            .onResourceError(errorHandler::dispatch)
            .launch("toggle")
    }
}
