package dev.shushant.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("description")
    val description: String? = "", // Invalid App ID provided. Please sign up at https://openexchangerates.org/signup, or contact support@openexchangerates.org.
    @SerialName("error")
    val error: Boolean? = false, // true
    @SerialName("message")
    val message: String? = "", // invalid_app_id
    @SerialName("status")
    val status: Int? = 0 // 401
)