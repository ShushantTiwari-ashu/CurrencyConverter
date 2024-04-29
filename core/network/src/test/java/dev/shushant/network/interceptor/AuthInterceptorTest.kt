package dev.shushant.network.interceptor

import dev.shushant.network.BuildConfig
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthInterceptorTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK(relaxed = true)
    private lateinit var chain: Interceptor.Chain
    private lateinit var authInterceptor: AuthInterceptor

    @Before
    fun setup() {
        authInterceptor = AuthInterceptor()
    }

    @Test
    fun `test intercept adds app_id query parameter`() {
        val originalRequest =
            Request.Builder()
                .url(BuildConfig.BACKEND_URL)
                .build()

        val modifiedRequest =
            Request.Builder()
                .url("${BuildConfig.BACKEND_URL}?app_id=${BuildConfig.OPEN_EXCHANGE_APP_ID}")
                .build()

        every { chain.request() } returns originalRequest
        every { chain.proceed(match { it.url == modifiedRequest.url }) } returns createResponse()

        authInterceptor.intercept(chain)

        // Verify that chain.proceed() was called with the modified request
        verify { chain.proceed(match { it.url == modifiedRequest.url }) }
    }

    private fun createResponse(): Response {
        return Response.Builder()
            .request(Request.Builder().url(BuildConfig.BACKEND_URL).build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()
    }
}
