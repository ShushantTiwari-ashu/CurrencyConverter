package dev.shushant.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.shushant.database.dao.CurrencyDao
import dev.shushant.database.dao.CurrencyExchangeRatesDao
import dev.shushant.database.entity.CurrenciesEntity
import dev.shushant.database.entity.CurrenciesExchangeRateEntity
import dev.shushant.test.currencies
import dev.shushant.test.exchangeRates
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AppDatabaseTest {
    private lateinit var currencyDao: CurrencyDao
    private lateinit var currencyExchangeRatesDao: CurrencyExchangeRatesDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java,
        ).build()
        currencyDao = db.currencyDao()
        currencyExchangeRatesDao = db.currencyExchangeDao()
    }

    @Test
    fun fetch_currencies_from_database() = runTest {
        currencyDao.insertCurrencies(currency = CurrenciesEntity(currencies = currencies))
        val currencies = currencyDao.getCurrencies()

        assert(currencies?.currencies == dev.shushant.test.currencies)
    }

    @Test
    fun fetch_exchangeRates_from_database() = runTest {
        currencyExchangeRatesDao.insertExchangeRates(rates = CurrenciesExchangeRateEntity(rates = exchangeRates))
        val exchangeRate = currencyExchangeRatesDao.getExchangeRates()

        assert(exchangeRate?.rates == exchangeRates)
    }


}