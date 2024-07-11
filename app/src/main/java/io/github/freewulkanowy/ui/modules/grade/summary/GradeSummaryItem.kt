package io.github.freewulkanowy.ui.modules.grade.summary

import io.github.freewulkanowy.data.db.entities.GradeDescriptive
import io.github.freewulkanowy.data.db.entities.GradeSummary

data class GradeSummaryItem(
    val gradeSummary: GradeSummary,
    val gradeDescriptive: GradeDescriptive?
)
