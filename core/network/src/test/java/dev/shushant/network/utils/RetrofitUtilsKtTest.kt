package dev.shushant.network.utils

import dev.shushant.network.model.CurrenciesResponse
import dev.shushant.test.currencies
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response

class RetrofitUtilsKtTest {
    private val json = Json.Default

    @Test
    fun `safeApiCall with successful response`() =
        runTest {
            // Mock the API call with a successful response
            val apiCall: suspend () -> Response<CurrenciesResponse> = {
                Response.success(CurrenciesResponse(data = currencies))
            }
            val result = safeApiCall(json, apiCall)

            // Assert that the result is success and contains the expected response data
            assertEquals(Result.success(CurrenciesResponse(data = currencies)), result)
        }

    @Test
    fun `safeApiCall with unsuccessful response and error message`() =
        runTest {
            // Mock the API call with an unsuccessful response and error message
            val errorResponseBody = "{\"message\":\"Error message\"}".toResponseBody()
            val apiCall: suspend () -> Response<CurrenciesResponse> = {
                Response.error(400, errorResponseBody)
            }

            // Call the safeApiCall function
            val result = safeApiCall(json, apiCall)

            // Assert that the result is failure and contains the expected error message
            assertEquals("Error message", result.exceptionOrNull()?.message)
        }

    @Test
    fun `safeApiCall with empty response body`() =
        runTest {
            // Mock the API call with an unsuccessful response and empty response body
            val apiCall: suspend () -> Response<CurrenciesResponse> = {
                Response.success(null)
            }

            // Call the safeApiCall function
            val result = safeApiCall(json, apiCall)

            // Assert that the result is failure and contains the expected error message
            assertEquals("Empty response body", result.exceptionOrNull()?.message)
        }

    @Test
    fun `safeApiCall with exception`() =
        runTest {
            // Mock the API call with an exception
            val apiCall: suspend () -> Response<CurrenciesResponse> = {
                throw Exception("Network error")
            }

            // Call the safeApiCall function
            val result = safeApiCall(json, apiCall)

            // Assert that the result is failure and contains the expected error message
            assertEquals("Please check your internet connection...", result.exceptionOrNull()?.message)
        }
}
