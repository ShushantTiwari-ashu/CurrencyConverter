package dev.shushant.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("description")
    val description: String? = "",
    @SerialName("error")
    val error: Boolean? = false,
    @SerialName("message")
    val message: String? = "",
    @SerialName("status")
    val status: Int? = 0,
)
