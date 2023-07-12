package dev.shushant.network.utils

import dev.shushant.network.model.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.Response

suspend fun <T : Any> safeApiCall(
    json: Json,
    apiCall: suspend () -> Response<T>
): Result<T> {
    return runCatching {
        apiCall()
    }.mapCatching { response ->
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(Throwable("Empty response body"))
            }
        } else {
            Result.failure(
                Throwable(
                    response.errorBody()?.string()
                        ?.let { json.decodeFromString<ErrorResponse>(it) }?.message
                )
            )
        }
    }.getOrElse {
        Result.failure(Throwable("Please check your internet connection..."))
    }
}

