package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.HomeworkRepository
import io.github.freewulkanowy.data.waitForResult
import io.github.freewulkanowy.services.sync.notifications.NewHomeworkNotification
import io.github.freewulkanowy.utils.monday
import io.github.freewulkanowy.utils.nextOrSameSchoolDay
import io.github.freewulkanowy.utils.sunday
import kotlinx.coroutines.flow.first
import java.time.LocalDate.now
import javax.inject.Inject

class HomeworkWork @Inject constructor(
    private val homeworkRepository: HomeworkRepository,
    private val newHomeworkNotification: NewHomeworkNotification,
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        val startDate = now().nextOrSameSchoolDay.monday
        val endDate = startDate.sunday

        homeworkRepository.getHomework(
            student = student,
            semester = semester,
            start = startDate,
            end = endDate,
            forceRefresh = true,
            notify = notify,
        ).waitForResult()

        homeworkRepository.getHomeworkFromDatabase(
            semester = semester,
            start = startDate,
            end = endDate
        )
            .first()
            .filter { !it.isNotified }.let {
                if (it.isNotEmpty()) newHomeworkNotification.notify(it, student)

                homeworkRepository.updateHomework(it.onEach { homework ->
                    homework.isNotified = true
                })
            }
    }
}
