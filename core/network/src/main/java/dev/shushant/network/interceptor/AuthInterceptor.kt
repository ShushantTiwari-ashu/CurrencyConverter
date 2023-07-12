package dev.shushant.network.interceptor

import dev.shushant.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val modifiedUrl = originalUrl.newBuilder().apply {
            addQueryParameter("app_id", BuildConfig.OPEN_EXCHANGE_APP_ID)
        }.build()
        val modifiedRequestBuilder = originalRequest.newBuilder()
            .url(modifiedUrl)
        val request = modifiedRequestBuilder.build()
        return chain.proceed(request)
    }
}