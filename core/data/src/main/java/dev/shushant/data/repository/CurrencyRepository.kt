package dev.shushant.data.repository

import dev.shushant.model.Currencies
import dev.shushant.model.CurrencyExchangeRate

interface CurrencyRepository {
    suspend fun getCurrencies(): Result<Currencies>
    suspend fun getCurrencyExchangeRate(
        base: String,
        symbols: String
    ): Result<CurrencyExchangeRate>
}