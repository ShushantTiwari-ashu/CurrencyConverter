package dev.shushant.network.service

import dev.shushant.network.model.CurrencyExchangeRateResponse
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConverterNetworkApi {
    @GET("currencies.json")
    suspend fun getCurrencies(): Response<JsonObject>

    @GET("latest.json")
    suspend fun getCurrencyRates(
        @Query("base") base: String,
        @Query("symbols") symbols: String,
    ): Response<CurrencyExchangeRateResponse>
}
