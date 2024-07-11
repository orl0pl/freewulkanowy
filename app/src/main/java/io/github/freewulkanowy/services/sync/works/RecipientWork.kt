package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.dataOrNull
import io.github.freewulkanowy.data.db.entities.MailboxType
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.MessageRepository
import io.github.freewulkanowy.data.repositories.RecipientRepository
import io.github.freewulkanowy.data.toFirstResult
import javax.inject.Inject

class RecipientWork @Inject constructor(
    private val messageRepository: MessageRepository,
    private val recipientRepository: RecipientRepository
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        val mailboxes = messageRepository.getMailboxes(student, forceRefresh = true).toFirstResult()
        mailboxes.dataOrNull?.forEach {
            recipientRepository.refreshRecipients(student, it, MailboxType.EMPLOYEE)
        }
    }
}
