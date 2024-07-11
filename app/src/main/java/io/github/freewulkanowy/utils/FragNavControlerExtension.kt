package io.github.freewulkanowy.utils

import androidx.fragment.app.Fragment
import com.ncapdevi.fragnav.FragNavController
import io.github.freewulkanowy.ui.base.BaseView

inline fun FragNavController.setOnViewChangeListener(crossinline listener: (view: BaseView) -> Unit) {
    transactionListener = object : FragNavController.TransactionListener {
        override fun onFragmentTransaction(
            fragment: Fragment?,
            transactionType: FragNavController.TransactionType
        ) {
            fragment?.let { listener(it as BaseView) }
        }

        override fun onTabTransaction(fragment: Fragment?, index: Int) {
            fragment?.let { listener(it as BaseView) }
        }
    }
}

fun FragNavController.safelyPopFragments(depth: Int) {
    if (!isRootFragment) popFragments(depth)
}
