package io.github.freewulkanowy.ui.modules.attendance

import android.annotation.SuppressLint
import io.github.freewulkanowy.data.Resource
import io.github.freewulkanowy.data.db.entities.Attendance
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.flatResourceFlow
import io.github.freewulkanowy.data.logResourceStatus
import io.github.freewulkanowy.data.mapResourceData
import io.github.freewulkanowy.data.onResourceData
import io.github.freewulkanowy.data.onResourceError
import io.github.freewulkanowy.data.onResourceIntermediate
import io.github.freewulkanowy.data.onResourceLoading
import io.github.freewulkanowy.data.onResourceNotLoading
import io.github.freewulkanowy.data.onResourceSuccess
import io.github.freewulkanowy.data.repositories.AttendanceRepository
import io.github.freewulkanowy.data.repositories.PreferencesRepository
import io.github.freewulkanowy.data.repositories.SemesterRepository
import io.github.freewulkanowy.data.repositories.StudentRepository
import io.github.freewulkanowy.data.resourceFlow
import io.github.freewulkanowy.ui.base.BasePresenter
import io.github.freewulkanowy.ui.base.ErrorHandler
import io.github.freewulkanowy.utils.AnalyticsHelper
import io.github.freewulkanowy.utils.capitalise
import io.github.freewulkanowy.utils.getLastSchoolDayIfHoliday
import io.github.freewulkanowy.utils.isExcusableOrNotExcused
import io.github.freewulkanowy.utils.isHolidays
import io.github.freewulkanowy.utils.monday
import io.github.freewulkanowy.utils.nextSchoolDay
import io.github.freewulkanowy.utils.previousOrSameSchoolDay
import io.github.freewulkanowy.utils.previousSchoolDay
import io.github.freewulkanowy.utils.sunday
import io.github.freewulkanowy.utils.toFormattedString
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.LocalDate.ofEpochDay
import javax.inject.Inject

class AttendancePresenter @Inject constructor(
    errorHandler: ErrorHandler,
    studentRepository: StudentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val semesterRepository: SemesterRepository,
    private val prefRepository: PreferencesRepository,
    private val analytics: AnalyticsHelper
) : BasePresenter<AttendanceView>(errorHandler, studentRepository) {

    private var initialDate: LocalDate? = null
    private var isWeekendHasLessons: Boolean = false

    var currentDate: LocalDate? = null
        private set

    private lateinit var lastError: Throwable

    private val attendanceToExcuseList = mutableListOf<Attendance>()

    private var isVulcanExcusedFunctionEnabled = false

    fun onAttachView(view: AttendanceView, date: Long?) {
        super.onAttachView(view)
        view.initView()
        Timber.i("Attendance view was initialized")
        errorHandler.showErrorMessage = ::showErrorViewOnError
        currentDate = date?.let(::ofEpochDay)
        loadData()
    }

    fun onPreviousDay() {
        val date = if (isWeekendHasLessons) {
            currentDate?.minusDays(1)
        } else currentDate?.previousSchoolDay

        view?.finishActionMode()
        attendanceToExcuseList.clear()
        reloadView(date ?: return)
        loadData()
    }

    fun onNextDay() {
        val date = if (isWeekendHasLessons) {
            currentDate?.plusDays(1)
        } else currentDate?.nextSchoolDay

        view?.finishActionMode()
        attendanceToExcuseList.clear()
        reloadView(date ?: return)
        loadData()
    }

    fun onPickDate() {
        view?.showDatePickerDialog(currentDate ?: return)
    }

    fun onDateSet(year: Int, month: Int, day: Int) {
        reloadView(LocalDate.of(year, month, day))
        loadData()
    }

    fun onSwipeRefresh() {
        Timber.i("Force refreshing the attendance")
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
        Timber.i("Attendance view is reselected")
        view?.let { view ->
            if (view.currentStackSize == 1) {
                if (currentDate != initialDate) {
                    reloadView(initialDate ?: return)
                    loadData()
                } else if (!view.isViewEmpty) {
                    view.resetView()
                }
            } else {
                view.popView()
            }
        }
    }

    fun onMainViewChanged() {
        view?.finishActionMode()
    }

    fun onAttendanceItemSelected(attendance: Attendance) {
        view?.apply {
            if (!excuseActionMode) {
                Timber.i("Select attendance item ${attendance.id}")
                showAttendanceDialog(attendance)
            }
        }
    }

    fun onExcuseButtonClick() {
        view?.startActionMode()
    }

    fun onExcuseCheckboxSelect(attendanceItem: Attendance, checked: Boolean) {
        if (checked) attendanceToExcuseList.add(attendanceItem)
        else attendanceToExcuseList.remove(attendanceItem)
    }

    fun onExcuseSubmitButtonClick(): Boolean {
        view?.apply {
            return if (attendanceToExcuseList.isNotEmpty()) {
                showExcuseDialog()
                true
            } else {
                showMessage(excuseNoSelectionString)
                false
            }
        }
        return false
    }

    fun onExcuseDialogSubmit(reason: String) {
        view?.finishActionMode()

        if (attendanceToExcuseList.isEmpty()) return

        if (isVulcanExcusedFunctionEnabled) {
            excuseAbsence(
                reason = reason.takeIf { it.isNotBlank() },
                toExcuseList = attendanceToExcuseList.toList()
            )
        } else {
            val attendanceToExcuseNumbers = attendanceToExcuseList.map { it.number }

            view?.startSendMessageIntent(
                date = attendanceToExcuseList[0].date,
                numbers = attendanceToExcuseNumbers.joinToString(", "),
                reason = reason
            )
        }
    }

    fun onPrepareActionMode(): Boolean {
        view?.apply {
            showExcuseCheckboxes(true)
            showExcuseButton(false)
            enableSwipe(false)
            showDayNavigation(false)
        }
        attendanceToExcuseList.clear()
        return true
    }

    fun onDestroyActionMode() {
        view?.apply {
            showExcuseCheckboxes(false)
            showExcuseButton(true)
            enableSwipe(true)
            showDayNavigation(true)
        }
    }

    fun onSummarySwitchSelected(): Boolean {
        view?.openSummaryView()
        return true
    }

    fun onCalculatorSwitchSelected(): Boolean {
        view?.openCalculatorView()
        return true
    }

    private fun loadData(forceRefresh: Boolean = false) {
        Timber.i("Loading attendance data started")

        var isParent = false

        flatResourceFlow {
            val student = studentRepository.getCurrentStudent()
            isParent = student.isParent

            val semester = semesterRepository.getCurrentSemester(student)

            checkInitialAndCurrentDate(semester)
            attendanceRepository.getAttendance(
                student = student,
                semester = semester,
                start = currentDate ?: now(),
                end = currentDate ?: now(),
                forceRefresh = forceRefresh
            )
        }
            .logResourceStatus("load attendance")
            .onResourceLoading {
                view?.showExcuseButton(false)
            }
            .mapResourceData {
                if (prefRepository.isShowPresent) {
                    it
                } else {
                    it.filter { item -> !item.presence }
                }.sortedBy { item -> item.number }
            }
            .onResourceData {
                isWeekendHasLessons = isWeekendHasLessons || isWeekendHasLessons(it)

                view?.run {
                    enableSwipe(true)
                    showProgress(false)
                    showErrorView(false)
                    showEmpty(it.isEmpty())
                    showContent(it.isNotEmpty())
                    updateData(it)
                    reloadNavigation()
                }
            }
            .onResourceIntermediate { view?.showRefresh(true) }
            .onResourceSuccess {
                isVulcanExcusedFunctionEnabled = it.any { item -> item.excusable }
                val anyExcusables = it.any { it.isExcusableOrNotExcused }
                view?.showExcuseButton(anyExcusables && (isParent || isVulcanExcusedFunctionEnabled))

                analytics.logEvent(
                    "load_data",
                    "type" to "attendance",
                    "items" to it.size
                )
            }
            .onResourceNotLoading {
                view?.run {
                    showRefresh(false)
                    showProgress(false)
                    enableSwipe(true)
                }
            }
            .onResourceError(errorHandler::dispatch)
            .launch()
    }

    private suspend fun checkInitialAndCurrentDate(semester: Semester) {
        if (initialDate == null) {
            val lessons = attendanceRepository.getAttendanceFromDatabase(
                semester = semester,
                start = now().monday,
                end = now().sunday,
            ).firstOrNull().orEmpty()
            isWeekendHasLessons = isWeekendHasLessons(lessons)
            initialDate = getInitialDate(semester)
        }

        if (currentDate == null) {
            currentDate = initialDate
        }
    }

    private fun isWeekendHasLessons(
        lessons: List<Attendance>,
    ): Boolean = lessons.any {
        it.date.dayOfWeek in listOf(
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY,
        )
    }

    private fun getInitialDate(semester: Semester): LocalDate {
        val now = now()

        return when {
            now.isHolidays -> now.getLastSchoolDayIfHoliday(semester.schoolYear)
            isWeekendHasLessons -> now
            else -> now.previousOrSameSchoolDay
        }
    }

    private fun excuseAbsence(reason: String?, toExcuseList: List<Attendance>) {
        resourceFlow {
            val student = studentRepository.getCurrentStudent()
            val semester = semesterRepository.getCurrentSemester(student)
            attendanceRepository.excuseForAbsence(student, semester, toExcuseList, reason)
        }.onEach {
            when (it) {
                is Resource.Loading -> view?.run {
                    Timber.i("Excusing absence started")
                    showProgress(true)
                    showContent(false)
                    showExcuseButton(false)
                }

                is Resource.Success -> {
                    Timber.i("Excusing for absence result: Success")
                    analytics.logEvent("excuse_absence", "items" to attendanceToExcuseList.size)
                    attendanceToExcuseList.clear()
                    view?.run {
                        showExcuseButton(false)
                        showMessage(excuseSuccessString)
                        showContent(true)
                        showProgress(false)
                    }
                    loadData(forceRefresh = true)
                }

                is Resource.Error -> {
                    Timber.i("Excusing for absence result: An exception occurred")
                    errorHandler.dispatch(it.error)
                    loadData()
                }
            }
        }.launch("excuse")
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

    private fun reloadView(date: LocalDate) {
        currentDate = date

        Timber.i("Reload attendance view with the date ${currentDate?.toFormattedString()}")
        view?.apply {
            showProgress(true)
            enableSwipe(false)
            showRefresh(false)
            showContent(false)
            showEmpty(false)
            showErrorView(false)
            clearData()
            reloadNavigation()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun reloadNavigation() {
        val currentDate = currentDate ?: return

        view?.apply {
            showPreButton(!currentDate.minusDays(1).isHolidays)
            showNextButton(!currentDate.plusDays(1).isHolidays)
            updateNavigationDay(currentDate.toFormattedString("EEEE, dd.MM").capitalise())
            showNavigation(true)
        }
    }
}
