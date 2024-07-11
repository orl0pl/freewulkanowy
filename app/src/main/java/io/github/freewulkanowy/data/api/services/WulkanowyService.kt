package io.github.freewulkanowy.data.api.services

import io.github.freewulkanowy.data.api.models.Mapping
import io.github.freewulkanowy.data.db.entities.AdminMessage
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface WulkanowyService {

    @GET("/Pengwius/wulkanowy-messages/master/dist/v1.json")
    suspend fun getAdminMessages(): List<AdminMessage>

    @GET("/Pengwius/wulkanowy-messages/master/dist/mapping4.json")
    suspend fun getMapping(): Mapping
}
