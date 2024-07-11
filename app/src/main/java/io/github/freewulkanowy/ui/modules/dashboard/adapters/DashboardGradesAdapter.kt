package io.github.freewulkanowy.ui.modules.dashboard.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.freewulkanowy.data.db.entities.Grade
import io.github.freewulkanowy.data.enums.GradeColorTheme
import io.github.freewulkanowy.databinding.SubitemDashboardGradesBinding
import io.github.freewulkanowy.databinding.SubitemDashboardSmallGradeBinding
import io.github.freewulkanowy.utils.getBackgroundColor
import io.github.freewulkanowy.utils.getCompatColor

class DashboardGradesAdapter : RecyclerView.Adapter<DashboardGradesAdapter.ViewHolder>() {

    var items = listOf<Pair<String, List<Grade>>>()

    lateinit var gradeColorTheme: GradeColorTheme

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        SubitemDashboardGradesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (subject, grades) = items[position]
        val context = holder.binding.root.context

        with(holder.binding) {
            dashboardGradesSubitemTitle.text = subject

            grades.forEach {
                val subitemBinding = SubitemDashboardSmallGradeBinding.inflate(
                    LayoutInflater.from(context),
                    dashboardGradesSubitemGradeContainer,
                    false
                )

                with(subitemBinding.dashboardSmallGradeSubitemValue) {
                    text = it.entry
                    backgroundTintList = ColorStateList.valueOf(
                        context.getCompatColor(it.getBackgroundColor(gradeColorTheme))
                    )
                }

                dashboardGradesSubitemGradeContainer.addView(subitemBinding.root)
            }
        }
    }

    class ViewHolder(val binding: SubitemDashboardGradesBinding) :
        RecyclerView.ViewHolder(binding.root)
}
