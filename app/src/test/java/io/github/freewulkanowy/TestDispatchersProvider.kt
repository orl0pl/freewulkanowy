package io.github.freewulkanowy

import io.github.freewulkanowy.utils.DispatchersProvider
import kotlinx.coroutines.Dispatchers

class TestDispatchersProvider : DispatchersProvider() {

    override val io get() = Dispatchers.Unconfined
}
