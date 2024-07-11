package io.github.freewulkanowy.data.api.services

import io.github.freewulkanowy.data.pojos.IntegrityRequest
import io.github.freewulkanowy.data.pojos.LoginEvent
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface SchoolsService {

    @POST("/log/loginEvent")
    suspend fun logLoginEvent(@Body request: IntegrityRequest<LoginEvent>)
}
