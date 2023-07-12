package dev.shushant.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shushant.network.BuildConfig
import dev.shushant.network.interceptor.AuthInterceptor
import dev.shushant.network.service.CurrencyConverterNetworkApi
import dev.shushant.network.source.NetworkDataSource
import dev.shushant.network.source.NetworkDataSourceImpl
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesAuthInterceptor(): Interceptor = AuthInterceptor()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BuildConfig.BACKEND_URL)
            .build()
    }

    @Provides
    @Singleton
    fun providesCurrencyConverterApi(retrofit: Retrofit): CurrencyConverterNetworkApi =
        retrofit.create()

    @Provides
    @Singleton
    fun providesNetworkDataSource(
        api: CurrencyConverterNetworkApi,
        json: Json,
    ): NetworkDataSource =
        NetworkDataSourceImpl(api, json)

}