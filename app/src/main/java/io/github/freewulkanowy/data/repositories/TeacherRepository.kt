package io.github.freewulkanowy.data.repositories

import io.github.freewulkanowy.data.WulkanowySdkFactory
import io.github.freewulkanowy.data.db.dao.TeacherDao
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.mappers.mapToEntities
import io.github.freewulkanowy.data.networkBoundResource
import io.github.freewulkanowy.utils.AutoRefreshHelper
import io.github.freewulkanowy.utils.getRefreshKey
import io.github.freewulkanowy.utils.uniqueSubtract
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherRepository @Inject constructor(
    private val teacherDb: TeacherDao,
    private val wulkanowySdkFactory: WulkanowySdkFactory,
    private val refreshHelper: AutoRefreshHelper,
) {

    private val saveFetchResultMutex = Mutex()

    private val cacheKey = "teachers"

    fun getTeachers(
        student: Student,
        semester: Semester,
        forceRefresh: Boolean,
    ) = networkBoundResource(
        mutex = saveFetchResultMutex,
        isResultEmpty = { it.isEmpty() },
        shouldFetch = {
            val isExpired = refreshHelper.shouldBeRefreshed(getRefreshKey(cacheKey, semester))
            it.isEmpty() || forceRefresh || isExpired
        },
        query = { teacherDb.loadAll(semester.studentId, semester.classId) },
        fetch = {
            wulkanowySdkFactory.create(student, semester)
                .getTeachers()
                .mapToEntities(semester)
        },
        saveFetchResult = { old, new ->
            teacherDb.removeOldAndSaveNew(
                oldItems = old uniqueSubtract new,
                newItems = new uniqueSubtract old,
            )
            refreshHelper.updateLastRefreshTimestamp(getRefreshKey(cacheKey, semester))
        }
    )
}
