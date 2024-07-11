package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.GradeStatisticsRepository
import io.github.freewulkanowy.data.waitForResult

import javax.inject.Inject

class GradeStatisticsWork @Inject constructor(
    private val gradeStatisticsRepository: GradeStatisticsRepository
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        with(gradeStatisticsRepository) {
            getGradesPartialStatistics(student, semester, "Wszystkie", forceRefresh = true).waitForResult()
            getGradesSemesterStatistics(student, semester, "Wszystkie", forceRefresh = true).waitForResult()
            getGradesPointsStatistics(student, semester, "Wszystkie", forceRefresh = true).waitForResult()
        }
    }
}
