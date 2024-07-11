package io.github.freewulkanowy.ui.modules.account.accountdetails

import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.db.entities.StudentWithSemesters
import io.github.freewulkanowy.ui.base.BaseView
import io.github.freewulkanowy.ui.modules.studentinfo.StudentInfoView

interface AccountDetailsView : BaseView {

    fun initView()

    fun showAccountData(student: Student)

    fun showAccountEditDetailsDialog(student: Student)

    fun showLogoutConfirmDialog()

    fun popViewToMain()

    fun popViewToAccounts()

    fun recreateMainView()

    fun enableSelectStudentButton(enable: Boolean)

    fun openStudentInfoView(
        infoType: StudentInfoView.Type,
        studentWithSemesters: StudentWithSemesters
    )

    fun showErrorView(show: Boolean)

    fun setErrorDetails(message: String)

    fun showProgress(show: Boolean)

    fun showContent(show: Boolean)
}
