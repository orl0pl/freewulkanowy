package io.github.freewulkanowy.ui.modules.grade

import io.github.freewulkanowy.data.db.entities.Grade
import io.github.freewulkanowy.data.db.entities.GradeDescriptive
import io.github.freewulkanowy.data.db.entities.GradeSummary

data class GradeSubject(
    val subject: String,
    val average: Double,
    val points: String,
    val summary: GradeSummary,
    val descriptive: GradeDescriptive?,
    val grades: List<Grade>,
    val isVulcanAverage: Boolean
)
