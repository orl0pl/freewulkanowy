package io.github.freewulkanowy.domain.timetable

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.repositories.TimetableRepository
import io.github.freewulkanowy.utils.monday
import io.github.freewulkanowy.utils.sunday
import java.time.LocalDate
import javax.inject.Inject

class IsStudentHasLessonsOnWeekendUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository,
    private val isWeekendHasLessonsUseCase: IsWeekendHasLessonsUseCase,
) {

    suspend operator fun invoke(
        semester: Semester,
        currentDate: LocalDate = LocalDate.now(),
    ): Boolean {
        val lessons = timetableRepository.getTimetableFromDatabase(
            semester = semester,
            start = currentDate.monday,
            end = currentDate.sunday,
        )
        return isWeekendHasLessonsUseCase(lessons)
    }
}
