package io.github.freewulkanowy.utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntegrityHelper @Inject constructor() {

    @Suppress("UNUSED_PARAMETER")
    fun getIntegrityToken(requestId: String): String? = null
}
