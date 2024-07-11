package io.github.freewulkanowy.data.repositories

import io.github.freewulkanowy.data.Resource
import io.github.freewulkanowy.data.api.models.Mapping
import io.github.freewulkanowy.data.api.services.WulkanowyService
import io.github.freewulkanowy.data.db.dao.AdminMessageDao
import io.github.freewulkanowy.data.db.entities.AdminMessage
import io.github.freewulkanowy.data.networkBoundResource
import io.github.freewulkanowy.utils.AutoRefreshHelper
import io.github.freewulkanowy.utils.getRefreshKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.sync.Mutex
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WulkanowyRepository @Inject constructor(
    private val wulkanowyService: WulkanowyService,
    private val adminMessageDao: AdminMessageDao,
    private val preferencesRepository: PreferencesRepository,
    private val refreshHelper: AutoRefreshHelper,
) {

    private val saveFetchResultMutex = Mutex()
    private val cacheKey = "mapping_refresh_key"

    fun getAdminMessages(): Flow<Resource<List<AdminMessage>>> =
        networkBoundResource(
            mutex = saveFetchResultMutex,
            isResultEmpty = { false },
            query = { adminMessageDao.loadAll() },
            fetch = { wulkanowyService.getAdminMessages() },
            shouldFetch = { true },
            saveFetchResult = { oldItems, newItems ->
                adminMessageDao.removeOldAndSaveNew(oldItems, newItems)
            },
        )
            .filterNot { it is Resource.Intermediate }

    suspend fun getMapping(): Mapping? {
        var savedMapping = preferencesRepository.mapping

        val isExpired = refreshHelper.shouldBeRefreshed(
            key = getRefreshKey(cacheKey)
        )

        if (savedMapping == null || isExpired) {
            fetchMapping()
            savedMapping = preferencesRepository.mapping
        }

        return savedMapping
    }

    suspend fun fetchMapping() {
        runCatching { wulkanowyService.getMapping() }
            .onFailure { Timber.e(it) }
            .onSuccess {
                preferencesRepository.mapping = it
                refreshHelper.updateLastRefreshTimestamp(cacheKey)
            }
    }
}
