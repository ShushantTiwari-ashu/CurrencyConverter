package dev.shushant.network.source

import dev.shushant.network.model.CurrenciesResponse
import dev.shushant.network.model.CurrencyExchangeRateResponse

interface NetworkDataSource {
    suspend fun getCurrencies(): Result<CurrenciesResponse>

    suspend fun getCurrencyExchangeRate(
        base: String,
        symbols: String,
    ): Result<CurrencyExchangeRateResponse>
}
