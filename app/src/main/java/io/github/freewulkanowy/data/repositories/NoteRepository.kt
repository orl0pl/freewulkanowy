package io.github.freewulkanowy.data.repositories

import io.github.freewulkanowy.data.WulkanowySdkFactory
import io.github.freewulkanowy.data.db.dao.NoteDao
import io.github.freewulkanowy.data.db.entities.Note
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.mappers.mapToEntities
import io.github.freewulkanowy.data.networkBoundResource
import io.github.freewulkanowy.utils.AutoRefreshHelper
import io.github.freewulkanowy.utils.getRefreshKey
import io.github.freewulkanowy.utils.toLocalDate
import io.github.freewulkanowy.utils.uniqueSubtract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDb: NoteDao,
    private val wulkanowySdkFactory: WulkanowySdkFactory,
    private val refreshHelper: AutoRefreshHelper,
) {

    private val saveFetchResultMutex = Mutex()

    private val cacheKey = "note"

    fun getNotes(
        student: Student,
        semester: Semester,
        forceRefresh: Boolean,
        notify: Boolean = false,
    ) = networkBoundResource(
        mutex = saveFetchResultMutex,
        isResultEmpty = { it.isEmpty() },
        shouldFetch = {
            val isExpired = refreshHelper.shouldBeRefreshed(
                getRefreshKey(cacheKey, semester)
            )
            it.isEmpty() || forceRefresh || isExpired
        },
        query = { noteDb.loadAll(student.studentId) },
        fetch = {
            wulkanowySdkFactory.create(student, semester)
                .getNotes()
                .mapToEntities(semester)
        },
        saveFetchResult = { old, new ->
            val notesToAdd = (new uniqueSubtract old).onEach {
                if (it.date >= student.registrationDate.toLocalDate()) it.apply {
                    isRead = false
                    if (notify) isNotified = false
                }
            }
            noteDb.removeOldAndSaveNew(
                oldItems = old uniqueSubtract new,
                newItems = notesToAdd,
            )
            refreshHelper.updateLastRefreshTimestamp(getRefreshKey(cacheKey, semester))
        }
    )

    fun getNotesFromDatabase(student: Student): Flow<List<Note>> {
        return noteDb.loadAll(student.studentId)
    }

    suspend fun updateNote(note: Note) {
        noteDb.updateAll(listOf(note))
    }

    suspend fun updateNotes(notes: List<Note>) {
        noteDb.updateAll(notes)
    }
}
