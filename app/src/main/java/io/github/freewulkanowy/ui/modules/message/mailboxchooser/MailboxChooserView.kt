package io.github.freewulkanowy.ui.modules.message.mailboxchooser

import io.github.freewulkanowy.data.db.entities.Mailbox
import io.github.freewulkanowy.ui.base.BaseView

interface MailboxChooserView : BaseView {

    fun initView()

    fun submitData(items: List<MailboxChooserItem>)

    fun onMailboxSelected(item: Mailbox?)
}
