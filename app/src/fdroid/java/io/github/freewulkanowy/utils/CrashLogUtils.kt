@file:Suppress("UNUSED_PARAMETER")

package io.github.freewulkanowy.utils

import timber.log.Timber

open class TimberTreeNoOp : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {}
}

class CrashLogTree : TimberTreeNoOp()

class CrashLogExceptionTree : TimberTreeNoOp()
