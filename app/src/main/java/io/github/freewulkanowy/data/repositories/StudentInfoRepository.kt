package io.github.freewulkanowy.data.repositories

import io.github.freewulkanowy.data.WulkanowySdkFactory
import io.github.freewulkanowy.data.db.dao.StudentInfoDao
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.mappers.mapToEntity
import io.github.freewulkanowy.data.networkBoundResource
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentInfoRepository @Inject constructor(
    private val studentInfoDao: StudentInfoDao,
    private val wulkanowySdkFactory: WulkanowySdkFactory,
) {

    private val saveFetchResultMutex = Mutex()

    fun getStudentInfo(
        student: Student,
        semester: Semester,
        forceRefresh: Boolean,
    ) = networkBoundResource(
        mutex = saveFetchResultMutex,
        isResultEmpty = { it == null },
        shouldFetch = { it == null || forceRefresh },
        query = { studentInfoDao.loadStudentInfo(student.studentId) },
        fetch = {
            wulkanowySdkFactory.create(student, semester)
                .getStudentInfo()
                .mapToEntity(semester)
        },
        saveFetchResult = { old, new ->
            if (old != null && new != old) {
                studentInfoDao.removeOldAndSaveNew(
                    oldItems = listOf(old),
                    newItems = listOf(new),
                )
            } else if (old == null) {
                studentInfoDao.insertAll(listOf(new))
            }
        }
    )
}
