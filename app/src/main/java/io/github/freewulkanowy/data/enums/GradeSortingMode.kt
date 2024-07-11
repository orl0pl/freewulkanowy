package io.github.freewulkanowy.data.enums

enum class GradeSortingMode(val value: String) {
    ALPHABETIC("alphabetic"),
    DATE("date"),
    AVERAGE("average");

    companion object {
        fun getByValue(value: String) = values().find { it.value == value } ?: ALPHABETIC
    }
}
