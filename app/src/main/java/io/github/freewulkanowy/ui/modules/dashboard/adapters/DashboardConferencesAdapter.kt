package io.github.freewulkanowy.ui.modules.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.freewulkanowy.data.db.entities.Conference
import io.github.freewulkanowy.databinding.SubitemDashboardConferencesBinding
import io.github.freewulkanowy.utils.toFormattedString

class DashboardConferencesAdapter :
    RecyclerView.Adapter<DashboardConferencesAdapter.ViewHolder>() {

    var items = emptyList<Conference>()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        SubitemDashboardConferencesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        with(holder.binding) {
            dashboardHomeworkSubitemTime.text = item.date.toFormattedString("HH:mm dd.MM.yyyy")
            dashboardHomeworkSubitemTitle.text = item.title
        }
    }

    class ViewHolder(val binding: SubitemDashboardConferencesBinding) :
        RecyclerView.ViewHolder(binding.root)
}
