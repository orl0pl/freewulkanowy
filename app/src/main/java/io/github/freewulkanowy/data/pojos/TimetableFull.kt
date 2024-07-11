package io.github.freewulkanowy.data.pojos

import io.github.freewulkanowy.data.db.entities.Timetable
import io.github.freewulkanowy.data.db.entities.TimetableAdditional
import io.github.freewulkanowy.data.db.entities.TimetableHeader

data class TimetableFull(
    val lessons: List<Timetable>,
    val additional: List<TimetableAdditional>,
    val headers: List<TimetableHeader>,
)
