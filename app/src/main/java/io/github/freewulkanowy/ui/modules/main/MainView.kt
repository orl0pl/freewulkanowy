package io.github.freewulkanowy.ui.modules.main

import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.db.entities.StudentWithSemesters
import io.github.freewulkanowy.ui.base.BaseView
import io.github.freewulkanowy.ui.modules.Destination
import io.github.freewulkanowy.ui.modules.settings.appearance.menuorder.AppMenuItem

interface MainView : BaseView {

    val isRootView: Boolean

    val currentViewTitle: String?

    val currentViewSubtitle: String?

    val currentStackSize: Int?

    fun initView(
        startMenuIndex: Int,
        rootAppMenuItems: List<AppMenuItem>,
        rootUpdatedDestinations: List<Destination>
    )

    fun switchMenuView(position: Int)

    fun showHomeArrow(show: Boolean)

    fun showAccountPicker(studentWithSemesters: List<StudentWithSemesters>)

    fun showBottomNavigation(show: Boolean)

    fun notifyMenuViewReselected()

    fun notifyMenuViewChanged()

    fun setViewTitle(title: String)

    fun setViewSubTitle(subtitle: String?)

    fun popView(depth: Int = 1)

    fun showStudentAvatar(student: Student)

    fun showInAppReview()

    fun showAppSupport()

    fun openMoreDestination(destination: Destination)

    interface MainChildView {

        fun onFragmentReselected()

        fun onFragmentChanged() {}
    }

    interface TitledView {

        val titleStringId: Int

        var subtitleString: String
            get() = ""
            set(_) {}
    }
}
