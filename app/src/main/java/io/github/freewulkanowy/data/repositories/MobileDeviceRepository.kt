package io.github.freewulkanowy.data.repositories

import io.github.freewulkanowy.data.WulkanowySdkFactory
import io.github.freewulkanowy.data.db.dao.MobileDeviceDao
import io.github.freewulkanowy.data.db.entities.MobileDevice
import io.github.freewulkanowy.data.db.entities.Semester
import io.github.freewulkanowy.data.db.entities.Student
import io.github.freewulkanowy.data.mappers.mapToEntities
import io.github.freewulkanowy.data.mappers.mapToMobileDeviceToken
import io.github.freewulkanowy.data.networkBoundResource
import io.github.freewulkanowy.data.pojos.MobileDeviceToken
import io.github.freewulkanowy.utils.AutoRefreshHelper
import io.github.freewulkanowy.utils.getRefreshKey
import io.github.freewulkanowy.utils.uniqueSubtract
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MobileDeviceRepository @Inject constructor(
    private val mobileDb: MobileDeviceDao,
    private val wulkanowySdkFactory: WulkanowySdkFactory,
    private val refreshHelper: AutoRefreshHelper,
) {

    private val saveFetchResultMutex = Mutex()

    private val cacheKey = "devices"

    fun getDevices(
        student: Student,
        semester: Semester,
        forceRefresh: Boolean,
    ) = networkBoundResource(
        mutex = saveFetchResultMutex,
        isResultEmpty = { it.isEmpty() },
        shouldFetch = {
            val isExpired = refreshHelper.shouldBeRefreshed(getRefreshKey(cacheKey, student))
            it.isEmpty() || forceRefresh || isExpired
        },
        query = { mobileDb.loadAll(student.studentId) },
        fetch = {
            wulkanowySdkFactory.create(student, semester)
                .getRegisteredDevices()
                .mapToEntities(student)
        },
        saveFetchResult = { old, new ->
            mobileDb.removeOldAndSaveNew(
                oldItems = old uniqueSubtract new,
                newItems = new uniqueSubtract old,
            )
            refreshHelper.updateLastRefreshTimestamp(getRefreshKey(cacheKey, student))
        }
    )

    suspend fun unregisterDevice(student: Student, semester: Semester, device: MobileDevice) {
        wulkanowySdkFactory.create(student, semester)
            .unregisterDevice(device.deviceId)

        mobileDb.deleteAll(listOf(device))
    }

    suspend fun getToken(student: Student, semester: Semester): MobileDeviceToken {
        return wulkanowySdkFactory.create(student, semester)
            .getToken()
            .mapToMobileDeviceToken()
    }
}
