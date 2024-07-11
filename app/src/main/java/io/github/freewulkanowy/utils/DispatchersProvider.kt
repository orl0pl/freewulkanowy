package io.github.freewulkanowy.utils

import kotlinx.coroutines.Dispatchers

open class DispatchersProvider {

    open val io get() = Dispatchers.IO
}
