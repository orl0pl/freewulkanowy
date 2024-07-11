package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.LuckyNumberRepository
import io.github.freewulkanowy.data.waitForResult
import io.github.freewulkanowy.services.sync.notifications.NewLuckyNumberNotification
import javax.inject.Inject

class LuckyNumberWork @Inject constructor(
    private val luckyNumberRepository: LuckyNumberRepository,
    private val newLuckyNumberNotification: NewLuckyNumberNotification,
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        luckyNumberRepository.getLuckyNumber(
            student = student,
            forceRefresh = true,
            notify = notify,
        ).waitForResult()

        luckyNumberRepository.getNotNotifiedLuckyNumber(student)?.let {
            newLuckyNumberNotification.notify(it, student)
            luckyNumberRepository.updateLuckyNumber(it.apply { isNotified = true })
        }
    }
}
