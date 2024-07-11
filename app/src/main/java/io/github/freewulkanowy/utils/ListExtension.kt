package io.github.freewulkanowy.utils

infix fun <T> List<T>.uniqueSubtract(other: List<T>): List<T> {
    val list = toMutableList()
    other.forEach {
        list.remove(it)
    }
    return list.toList()
}
