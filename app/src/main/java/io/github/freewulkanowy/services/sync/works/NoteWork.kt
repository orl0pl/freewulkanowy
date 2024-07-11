package io.github.freewulkanowy.services.sync.works

import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.repositories.NoteRepository
import io.github.freewulkanowy.data.waitForResult
import io.github.freewulkanowy.services.sync.notifications.NewNoteNotification
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NoteWork @Inject constructor(
    private val noteRepository: NoteRepository,
    private val newNoteNotification: NewNoteNotification,
) : Work {

    override suspend fun doWork(student: Student, semester: Semester, notify: Boolean) {
        noteRepository.getNotes(
            student = student,
            semester = semester,
            forceRefresh = true,
            notify = notify,
        ).waitForResult()

        noteRepository.getNotesFromDatabase(student).first()
            .filter { !it.isNotified }.let {
                if (it.isNotEmpty()) newNoteNotification.notify(it, student)

                noteRepository.updateNotes(it.onEach { note -> note.isNotified = true })
            }
    }
}

