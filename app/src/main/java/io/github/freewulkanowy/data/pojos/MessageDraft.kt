package io.github.freewulkanowy.data.pojos

import io.github.freewulkanowy.ui.modules.message.send.RecipientChipItem
import kotlinx.serialization.Serializable

@Serializable
data class MessageDraft(
    val recipients: List<RecipientChipItem>,
    val subject: String,
    val content: String,
)
