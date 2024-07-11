package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.ExamRepository
import io.github.freewulkanowy.data.waitForResult
import io.github.freewulkanowy.services.sync.notifications.NewExamNotification
import io.github.freewulkanowy.utils.endExamsDay
import io.github.freewulkanowy.utils.startExamsDay
import kotlinx.coroutines.flow.first
import java.time.LocalDate.now
import javax.inject.Inject

class ExamWork @Inject constructor(
    private val examRepository: ExamRepository,
    private val newExamNotification: NewExamNotification,
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        val startDate = now().startExamsDay
        val endDate = startDate.endExamsDay

        examRepository.getExams(
            student = student,
            semester = semester,
            start = startDate,
            end = endDate,
            forceRefresh = true,
            notify = notify,
        ).waitForResult()

        examRepository.getExamsFromDatabase(
            semester = semester,
            start = startDate,
            end = endDate,
        )
            .first()
            .filter { !it.isNotified }.let {
                if (it.isNotEmpty()) newExamNotification.notify(it, student)

                examRepository.updateExam(it.onEach { exam -> exam.isNotified = true })
            }
    }
}
