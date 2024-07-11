package io.github.freewulkanowy.ui.modules.message.send

import io.github.freewulkanowy.data.db.entities.Recipient
import io.github.wulkanowy.materialchipsinput.ChipItem
import kotlinx.serialization.Serializable

@Serializable
data class RecipientChipItem(

    override val title: String,

    override val summary: String,

    val recipient: Recipient

) : ChipItem
