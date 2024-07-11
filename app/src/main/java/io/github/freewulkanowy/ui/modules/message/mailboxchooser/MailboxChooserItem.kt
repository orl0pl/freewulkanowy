package io.github.freewulkanowy.ui.modules.message.mailboxchooser

import io.github.freewulkanowy.data.db.entities.Mailbox

data class MailboxChooserItem(
    val mailbox: Mailbox? = null,
    val isAll: Boolean = false,
    val onClickListener: (Mailbox?) -> Unit,
)
