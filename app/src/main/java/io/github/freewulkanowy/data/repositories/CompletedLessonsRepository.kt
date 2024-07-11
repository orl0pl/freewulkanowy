package io.github.freewulkanowy.data.repositories

import io.github.freewulkanowy.data.WulkanowySdkFactory
import io.github.freewulkanowy.data.db.dao.CompletedLessonsDao
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.mappers.mapToEntities
import io.github.freewulkanowy.data.networkBoundResource
import io.github.freewulkanowy.utils.AutoRefreshHelper
import io.github.freewulkanowy.utils.getRefreshKey
import io.github.freewulkanowy.utils.monday
import io.github.freewulkanowy.utils.sunday
import io.github.freewulkanowy.utils.uniqueSubtract
import kotlinx.coroutines.sync.Mutex
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompletedLessonsRepository @Inject constructor(
    private val completedLessonsDb: CompletedLessonsDao,
    private val wulkanowySdkFactory: WulkanowySdkFactory,
    private val refreshHelper: AutoRefreshHelper,
) {

    private val saveFetchResultMutex = Mutex()

    private val cacheKey = "completed"

    fun getCompletedLessons(
        student: Student,
        semester: Semester,
        start: LocalDate,
        end: LocalDate,
        forceRefresh: Boolean,
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
            completedLessonsDb.loadAll(
                studentId = semester.studentId,
                diaryId = semester.diaryId,
                from = start.monday,
                end = end.sunday
            )
        },
        fetch = {
            wulkanowySdkFactory.create(student, semester)
                .getCompletedLessons(start.monday, end.sunday)
                .mapToEntities(semester)
        },
        saveFetchResult = { old, new ->
            completedLessonsDb.removeOldAndSaveNew(
                oldItems = old uniqueSubtract new,
                newItems = new uniqueSubtract old,
            )
            refreshHelper.updateLastRefreshTimestamp(getRefreshKey(cacheKey, semester, start, end))
        },
        filterResult = { it.filter { item -> item.date in start..end } }
    )
}
