package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.SchoolAnnouncementRepository
import io.github.freewulkanowy.data.waitForResult
import io.github.freewulkanowy.services.sync.notifications.NewSchoolAnnouncementNotification
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

class SchoolAnnouncementWork @Inject constructor(
    private val schoolAnnouncementRepository: SchoolAnnouncementRepository,
    private val newSchoolAnnouncementNotification: NewSchoolAnnouncementNotification,
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        schoolAnnouncementRepository.getSchoolAnnouncements(
            student = student,
            forceRefresh = true,
            notify = notify,
        ).waitForResult()

        schoolAnnouncementRepository.getSchoolAnnouncementFromDatabase(student)
            .first()
            .filter { !it.isNotified && it.date >= LocalDate.now() }
            .let {
                if (it.isNotEmpty()) {
                    newSchoolAnnouncementNotification.notify(it, student)
                }

                schoolAnnouncementRepository.updateSchoolAnnouncement(it.onEach { schoolAnnouncement ->
                    schoolAnnouncement.isNotified = true
                })
            }
    }
}
