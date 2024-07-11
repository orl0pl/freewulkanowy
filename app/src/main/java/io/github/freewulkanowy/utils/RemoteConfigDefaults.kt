package io.github.freewulkanowy.utils

enum class RemoteConfigDefaults(val value: Any) {
    USER_AGENT_TEMPLATE(""),
    ;

    val key get() = name.lowercase()
}
