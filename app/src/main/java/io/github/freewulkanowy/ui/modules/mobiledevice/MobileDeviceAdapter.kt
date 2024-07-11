package io.github.freewulkanowy.ui.modules.mobiledevice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.freewulkanowy.data.db.entities.MobileDevice
import io.github.freewulkanowy.databinding.ItemMobileDeviceBinding
import io.github.freewulkanowy.utils.toFormattedString
import javax.inject.Inject

class MobileDeviceAdapter @Inject constructor() :
    RecyclerView.Adapter<MobileDeviceAdapter.ItemViewHolder>() {

    var items = mutableListOf<MobileDevice>()

    var onDeviceUnregisterListener: (device: MobileDevice, position: Int) -> Unit = { _, _ -> }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        ItemMobileDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val device = items[position]

        with(holder.binding) {
            mobileDeviceItemDate.text = device.date.toFormattedString("dd.MM.yyyy HH:mm:ss")
            mobileDeviceItemName.text = device.name
            mobileDeviceItemUnregister.setOnClickListener {
                onDeviceUnregisterListener(device, position)
            }
        }
    }

    class ItemViewHolder(val binding: ItemMobileDeviceBinding) :
        RecyclerView.ViewHolder(binding.root)
}
