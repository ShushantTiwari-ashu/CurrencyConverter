package dev.shushant.data

import dev.shushant.database.dao.CurrencyExchangeRatesDao
import dev.shushant.database.entity.CurrenciesExchangeRateEntity

class TestCurrencyExchangeRateDao : CurrencyExchangeRatesDao {
    private var exchangeRates: CurrenciesExchangeRateEntity? = null
    override suspend fun insert(rates: CurrenciesExchangeRateEntity): Long {
        this.exchangeRates = rates
        return 0
    }

    override suspend fun deleteRecord() {
        exchangeRates = null
    }

    override fun getExchangeRates(): CurrenciesExchangeRateEntity? {
        return exchangeRates
    }

    override suspend fun insertExchangeRates(rates: CurrenciesExchangeRateEntity) {
        deleteRecord()
        insert(rates)
    }
}