package dev.shushant.network.source

import dev.shushant.network.model.CurrenciesResponse
import dev.shushant.network.model.CurrencyExchangeRateResponse
import dev.shushant.network.service.CurrencyConverterNetworkApi
import dev.shushant.network.utils.safeApiCall
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import javax.inject.Inject

class NetworkDataSourceImpl
    @Inject
    constructor(
        private val service: CurrencyConverterNetworkApi,
        private val json: Json,
    ) : NetworkDataSource {
        override suspend fun getCurrencies(): Result<CurrenciesResponse> {
            return safeApiCall(json) {
                service.getCurrencies()
            }.mapCatching { it.toCurrencyResponse(json) }
        }

        override suspend fun getCurrencyExchangeRate(
            base: String,
            symbols: String,
        ): Result<CurrencyExchangeRateResponse> {
            return safeApiCall(json) {
                service.getCurrencyRates(base = base, symbols = symbols)
            }
        }
    }

private fun JsonObject.toCurrencyResponse(json: Json): CurrenciesResponse {
    return json.decodeFromJsonElement(json.parseToJsonElement(this.toString()))
}
