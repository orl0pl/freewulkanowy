package io.github.freewulkanowy.data.enums

enum class AppTheme(val value: String) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark"),
    BLACK("black");

    companion object {
        fun getByValue(value: String) = values().find { it.value == value } ?: LIGHT
    }
}
