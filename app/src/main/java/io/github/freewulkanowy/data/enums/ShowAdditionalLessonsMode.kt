package io.github.freewulkanowy.data.enums

enum class ShowAdditionalLessonsMode(val value: String) {
    NONE("none"),
    INLINE("inline"),
    BELOW("below");

    companion object {
        fun getByValue(value: String) = entries.find { it.value == value } ?: INLINE
    }
}
