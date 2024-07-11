package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.TimetableRepository
import io.github.freewulkanowy.data.waitForResult
import io.github.freewulkanowy.services.sync.notifications.ChangeTimetableNotification
import io.github.freewulkanowy.utils.nextOrSameSchoolDay
import java.time.LocalDate.now
import javax.inject.Inject

class TimetableWork @Inject constructor(
    private val timetableRepository: TimetableRepository,
    private val changeTimetableNotification: ChangeTimetableNotification,
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        val startDate = now().nextOrSameSchoolDay
        val endDate = startDate.plusDays(7)

        timetableRepository.getTimetable(
            student = student,
            semester = semester,
            start = startDate,
            end = endDate,
            forceRefresh = true,
            notify = notify,
        )
            .waitForResult()

        timetableRepository.getTimetableFromDatabase(
            semester = semester,
            start = startDate,
            end = endDate,
        )
            .filterNot { it.isNotified }
            .let {
                if (it.isNotEmpty()) changeTimetableNotification.notify(it, student)

                timetableRepository.updateTimetable(it.onEach { timetable ->
                    timetable.isNotified = true
                })
            }
    }
}
