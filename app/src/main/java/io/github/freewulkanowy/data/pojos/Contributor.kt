package io.github.freewulkanowy.data.pojos

import kotlinx.serialization.Serializable

@Serializable
class Contributor(
    val displayName: String,
    val githubUsername: String
)
