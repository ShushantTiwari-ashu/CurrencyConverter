package dev.shushant.database.model

import dev.shushant.database.dao.CurrencyDao
import dev.shushant.database.dao.CurrencyExchangeRatesDao

data class LocalDataSource(
    val currencyDao: CurrencyDao,
    val currencyExchangeRatesDao: CurrencyExchangeRatesDao,
)
