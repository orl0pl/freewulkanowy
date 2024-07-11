package io.github.freewulkanowy.ui.modules.account

import io.github.freewulkanowy.data.db.entities.StudentWithSemesters
import io.github.freewulkanowy.data.logResourceStatus
import io.github.freewulkanowy.data.onResourceError
import io.github.freewulkanowy.data.onResourceSuccess
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.data.resourceFlow
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import timber.log.Timber
import javax.inject.Inject

class AccountPresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
) : BasePresenter<AccountView>(errorHandler, studentRepository) {

    override fun onAttachView(view: AccountView) {
        super.onAttachView(view)
        view.initView()
        Timber.i("Account view was initialized")
        loadData()
    }

    fun onAddSelected() {
        Timber.i("Select add account")
        view?.openLoginView()
    }

    fun onItemSelected(studentWithSemesters: StudentWithSemesters) {
        view?.openAccountDetailsView(studentWithSemesters.student)
    }

    private fun loadData() {
        resourceFlow { studentRepository.getSavedStudents(false) }
            .logResourceStatus("load account data")
            .onResourceSuccess { view?.updateData(createAccountItems(it)) }
            .onResourceError(errorHandler::dispatch)
            .launch("load")
    }

    private fun createAccountItems(items: List<StudentWithSemesters>): List<AccountItem<*>> {
        return items.groupBy {
            Account("${it.student.userName} (${it.student.email})", it.student.isParent)
        }
            .map { (account, students) ->
                listOf(
                    AccountItem(account, AccountItem.ViewType.HEADER)
                ) + students.map { student ->
                    AccountItem(student, AccountItem.ViewType.ITEM)
                }
            }
            .flatten()
    }
}
