package io.github.freewulkanowy.domain.timetable

import io.github.freewulkanowy.data.db.entities.Timetable
import java.time.DayOfWeek
import javax.inject.Inject

class IsWeekendHasLessonsUseCase @Inject constructor() {

    operator fun invoke(
        lessons: List<Timetable>,
    ): Boolean = lessons.any {
        it.date.dayOfWeek in listOf(
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY,
        )
    }
}
