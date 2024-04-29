package dev.shushant.data

import dev.shushant.database.dao.CurrencyDao
import dev.shushant.database.entity.CurrenciesEntity

class TestCurrencyDao : CurrencyDao {
    private var currency: CurrenciesEntity? = null

    override suspend fun insert(currency: CurrenciesEntity): Long {
        this.currency = currency
        return 0
    }

    override suspend fun deleteRecord() {
        currency = null
    }

    override fun getCurrencies(): CurrenciesEntity? {
        return currency
    }

    override suspend fun insertCurrencies(currency: CurrenciesEntity) {
        deleteRecord()
        insert(currency)
    }
}
