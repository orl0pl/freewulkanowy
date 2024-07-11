package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.AttendanceSummaryRepository
import io.github.freewulkanowy.data.waitForResult
import javax.inject.Inject

class AttendanceSummaryWork @Inject constructor(
    private val attendanceSummaryRepository: AttendanceSummaryRepository
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        attendanceSummaryRepository.getAttendanceSummary(
            student = student,
            semester = semester,
            subjectId = -1,
            forceRefresh = true,
        ).waitForResult()
    }
}
