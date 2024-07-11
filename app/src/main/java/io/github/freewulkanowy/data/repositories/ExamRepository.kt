package io.github.freewulkanowy.data.repositories

import io.github.freewulkanowy.data.WulkanowySdkFactory
import io.github.freewulkanowy.data.db.dao.ExamDao
import io.github.freewulkanowy.data.db.entities.Exam
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.mappers.mapToEntities
import io.github.freewulkanowy.data.networkBoundResource
import io.github.freewulkanowy.utils.AutoRefreshHelper
import io.github.freewulkanowy.utils.endExamsDay
import io.github.freewulkanowy.utils.getRefreshKey
import io.github.freewulkanowy.utils.startExamsDay
import io.github.freewulkanowy.utils.uniqueSubtract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamRepository @Inject constructor(
    private val examDb: ExamDao,
    private val wulkanowySdkFactory: WulkanowySdkFactory,
    private val refreshHelper: AutoRefreshHelper,
) {

    private val saveFetchResultMutex = Mutex()

    private val cacheKey = "exam"

    fun getExams(
        student: Student,
        semester: Semester,
        start: LocalDate,
        end: LocalDate,
        forceRefresh: Boolean,
        notify: Boolean = false,
    ) = networkBoundResource(
        mutex = saveFetchResultMutex,
        isResultEmpty = { it.isEmpty() },
        shouldFetch = {
            val isExpired = refreshHelper.shouldBeRefreshed(
                key = getRefreshKey(cacheKey, semester, start, end)
            )
            it.isEmpty() || forceRefresh || isExpired
        },
        query = {
            examDb.loadAll(
                diaryId = semester.diaryId,
                studentId = semester.studentId,
                from = start.startExamsDay,
                end = start.endExamsDay
            )
        },
        fetch = {
            wulkanowySdkFactory.create(student, semester)
                .getExams(start.startExamsDay, start.endExamsDay)
                .mapToEntities(semester)
        },
        saveFetchResult = { old, new ->
            examDb.removeOldAndSaveNew(
                oldItems = old uniqueSubtract new,
                newItems = (new uniqueSubtract old).onEach {
                    if (notify) it.isNotified = false
                },
            )
            refreshHelper.updateLastRefreshTimestamp(getRefreshKey(cacheKey, semester, start, end))
        },
        filterResult = { it.filter { item -> item.date in start..end } }
    )

    fun getExamsFromDatabase(
        semester: Semester,
        start: LocalDate,
        end: LocalDate
    ): Flow<List<Exam>> = examDb.loadAll(
        diaryId = semester.diaryId,
        studentId = semester.studentId,
        from = start,
        end = end,
    )

    suspend fun updateExam(exam: List<Exam>) = examDb.updateAll(exam)
}
