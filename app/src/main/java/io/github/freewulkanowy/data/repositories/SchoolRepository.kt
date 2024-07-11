package io.github.freewulkanowy.data.repositories

import io.github.freewulkanowy.data.WulkanowySdkFactory
import io.github.freewulkanowy.data.db.dao.SchoolDao
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.mappers.mapToEntity
import io.github.freewulkanowy.data.networkBoundResource
import io.github.freewulkanowy.utils.AutoRefreshHelper
import io.github.freewulkanowy.utils.getRefreshKey
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolRepository @Inject constructor(
    private val schoolDb: SchoolDao,
    private val wulkanowySdkFactory: WulkanowySdkFactory,
    private val refreshHelper: AutoRefreshHelper,
) {

    private val saveFetchResultMutex = Mutex()

    private val cacheKey = "school_info"

    fun getSchoolInfo(
        student: Student,
        semester: Semester,
        forceRefresh: Boolean,
    ) = networkBoundResource(
        mutex = saveFetchResultMutex,
        isResultEmpty = { it == null },
        shouldFetch = {
            val isExpired = refreshHelper.shouldBeRefreshed(
                key = getRefreshKey(cacheKey, student)
            )
            it == null || forceRefresh || isExpired
        },
        query = { schoolDb.load(semester.studentId, semester.classId) },
        fetch = {
            wulkanowySdkFactory.create(student, semester)
                .getSchool()
                .mapToEntity(semester)
        },
        saveFetchResult = { old, new ->
            if (old != null && new != old) {
                schoolDb.removeOldAndSaveNew(
                    oldItems = listOf(old),
                    newItems = listOf(new)
                )
            } else if (old == null) {
                schoolDb.insertAll(listOf(new))
            }
            refreshHelper.updateLastRefreshTimestamp(getRefreshKey(cacheKey, student))
        }
    )
}
