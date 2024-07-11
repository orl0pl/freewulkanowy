package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.CompletedLessonsRepository
import io.github.freewulkanowy.data.waitForResult
import io.github.freewulkanowy.utils.monday
import io.github.freewulkanowy.utils.sunday
import java.time.LocalDate.now
import javax.inject.Inject

class CompletedLessonWork @Inject constructor(
    private val completedLessonsRepository: CompletedLessonsRepository
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        completedLessonsRepository.getCompletedLessons(
            student = student,
            semester = semester,
            start = now().monday,
            end = now().sunday,
            forceRefresh = true,
        ).waitForResult()
    }
}
