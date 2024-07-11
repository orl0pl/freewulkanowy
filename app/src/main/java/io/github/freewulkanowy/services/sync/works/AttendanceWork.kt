package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.AttendanceRepository
import io.github.freewulkanowy.data.waitForResult
import io.github.freewulkanowy.services.sync.notifications.NewAttendanceNotification
import io.github.freewulkanowy.utils.previousOrSameSchoolDay
import kotlinx.coroutines.flow.first
import java.time.LocalDate.now
import javax.inject.Inject

class AttendanceWork @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val newAttendanceNotification: NewAttendanceNotification,
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        val startDate = now().previousOrSameSchoolDay
        val endDate = startDate.plusDays(7)

        attendanceRepository.getAttendance(
            student = student,
            semester = semester,
            start = startDate,
            end = endDate,
            forceRefresh = true,
            notify = notify,
        )
            .waitForResult()

        attendanceRepository.getAttendanceFromDatabase(
            semester = semester,
            start = startDate,
            end = endDate,
        )
            .first()
            .filterNot { it.isNotified }
            .let {
                if (it.isNotEmpty()) newAttendanceNotification.notify(it, student)

                attendanceRepository.updateTimetable(it.onEach { attendance ->
                    attendance.isNotified = true
                })
            }
    }
}
