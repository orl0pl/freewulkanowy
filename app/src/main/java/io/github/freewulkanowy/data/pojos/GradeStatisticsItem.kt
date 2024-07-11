package io.github.freewulkanowy.data.pojos

import io.github.freewulkanowy.data.db.entities.GradePartialStatistics
import io.github.freewulkanowy.data.db.entities.GradePointsStatistics
import io.github.freewulkanowy.data.db.entities.GradeSemesterStatistics

data class GradeStatisticsItem(

    val type: DataType,

    val average: String,

    val partial: GradePartialStatistics?,

    val semester: GradeSemesterStatistics?,

    val points: GradePointsStatistics?

) {
    enum class DataType {
        SEMESTER,
        PARTIAL,
        POINTS,
    }
}
