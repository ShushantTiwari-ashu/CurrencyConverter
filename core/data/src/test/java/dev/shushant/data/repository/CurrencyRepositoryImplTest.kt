package dev.shushant.data.repository

import dev.shushant.data.TestCurrencyDao
import dev.shushant.data.TestCurrencyExchangeRateDao
import dev.shushant.data.utils.AppDispatcher
import dev.shushant.database.entity.CurrenciesEntity
import dev.shushant.database.entity.CurrenciesExchangeRateEntity
import dev.shushant.database.model.LocalDataSource
import dev.shushant.model.Currencies
import dev.shushant.model.CurrencyExchangeRate
import dev.shushant.network.model.CurrenciesResponse
import dev.shushant.network.model.CurrencyExchangeRateResponse
import dev.shushant.network.source.NetworkDataSource
import dev.shushant.test.MainDispatcherRule
import dev.shushant.test.currencies
import dev.shushant.test.exchangeRates
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.minutes

class CurrencyRepositoryImplTest {
    @get:Rule(order = 1)
    val dispatcherRule = MainDispatcherRule()

    @get:Rule(order = 2)
    val mockkRule = MockKRule(this)

    private lateinit var currencyRepository: CurrencyRepository

    @MockK
    private lateinit var networkDataSource: NetworkDataSource

    @MockK
    private lateinit var dispatcher: AppDispatcher

    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup() {
        localDataSource =
            LocalDataSource(
                currencyDao = TestCurrencyDao(),
                currencyExchangeRatesDao = TestCurrencyExchangeRateDao(),
            )
        currencyRepository = CurrencyRepositoryImpl(networkDataSource, localDataSource, dispatcher)
        every { dispatcher.dispatcherIO }.returns(dispatcherRule.testDispatcher)
    }

    private fun setSuccessData() {
        coEvery { networkDataSource.getCurrencies() } returns Result.success(CurrenciesResponse(data = currencies))
        coEvery { networkDataSource.getCurrencyExchangeRate(any(), any()) } returns
            Result.success(
                CurrencyExchangeRateResponse(rates = exchangeRates),
            )
    }

    private fun setFailureData() {
        coEvery { networkDataSource.getCurrencies() } returns Result.failure(Throwable())
        coEvery { networkDataSource.getCurrencyExchangeRate(any(), any()) } returns
            Result.failure(
                Throwable(),
            )
    }

    @Test
    fun `fetch currencies from network if database does not have data`() =
        runTest(dispatcherRule.testDispatcher) {
            setSuccessData()
            val networkCurrencies = currencyRepository.getCurrencies()
            coVerify { networkDataSource.getCurrencies() }
            assert(
                networkCurrencies ==
                    Result.success(
                        Currencies(
                            currencies.map {
                                Currencies.Item(
                                    currencyCode = it.key,
                                    currencyName = it.value,
                                )
                            },
                        ),
                    ),
            )
        }

    @Test
    fun `fetch currencies from db if database has data`() =
        runTest(dispatcherRule.testDispatcher) {
            setSuccessData()
            localDataSource.currencyDao.insertCurrencies(CurrenciesEntity(currencies = currencies))
            val networkCurrencies = currencyRepository.getCurrencies()
            coVerify(inverse = true) { networkDataSource.getCurrencies() }
            assert(
                networkCurrencies ==
                    Result.success(
                        Currencies(
                            currencies.map {
                                Currencies.Item(
                                    currencyCode = it.key,
                                    currencyName = it.value,
                                )
                            },
                        ),
                    ),
            )
        }

    @Test
    fun `fetch currencies from network if database has data but it is outdated(30 minutes check)`() =
        runTest(dispatcherRule.testDispatcher) {
            setSuccessData()
            localDataSource.currencyDao.insertCurrencies(
                CurrenciesEntity(
                    currencies = currencies,
                    timeStamp =
                        Clock.System.now().minus(30.minutes)
                            .toEpochMilliseconds(),
                ),
            )
            val networkCurrencies = currencyRepository.getCurrencies()
            coVerify { networkDataSource.getCurrencies() }
            assert(
                networkCurrencies ==
                    Result.success(
                        Currencies(
                            currencies.map {
                                Currencies.Item(
                                    currencyCode = it.key,
                                    currencyName = it.value,
                                )
                            },
                        ),
                    ),
            )
        }

    @Test
    fun `test fetch currencies failure use case`() =
        runTest(dispatcherRule.testDispatcher) {
            setFailureData()
            val networkData = currencyRepository.getCurrencies()
            assert(networkData.isFailure)
        }

    @Test
    fun `fetch currency exchange rate from network if database does not have data`() =
        runTest(dispatcherRule.testDispatcher) {
            setSuccessData()
            val networkCurrenciesExchangeRate =
                currencyRepository.getCurrencyExchangeRate("USD", "")
            coVerify { networkDataSource.getCurrencyExchangeRate(any(), any()) }
            assert(
                networkCurrenciesExchangeRate ==
                    Result.success(
                        CurrencyExchangeRate(
                            rates = exchangeRates,
                            base = "USD",
                        ),
                    ),
            )
        }

    @Test
    fun `fetch currency exchange rate from db if database has data`() =
        runTest(dispatcherRule.testDispatcher) {
            setSuccessData()
            localDataSource.currencyExchangeRatesDao.insertExchangeRates(
                CurrenciesExchangeRateEntity(rates = exchangeRates),
            )
            val networkCurrenciesExchangeRate =
                currencyRepository.getCurrencyExchangeRate("USD", "")
            coVerify(inverse = true) { networkDataSource.getCurrencyExchangeRate(any(), any()) }
            assert(
                networkCurrenciesExchangeRate ==
                    Result.success(
                        CurrencyExchangeRate(
                            rates = exchangeRates,
                        ),
                    ),
            )
        }

    @Test
    fun `fetch currency exchange rate from network if database has data but it is outdated(30 minutes check)`() =
        runTest(dispatcherRule.testDispatcher) {
            setSuccessData()
            localDataSource.currencyExchangeRatesDao.insertExchangeRates(
                CurrenciesExchangeRateEntity(
                    timestamp =
                        Clock.System.now().minus(30.minutes)
                            .toEpochMilliseconds(),
                    rates = exchangeRates,
                ),
            )
            val networkCurrenciesExchangeRate =
                currencyRepository.getCurrencyExchangeRate("USD", "")
            coVerify { networkDataSource.getCurrencyExchangeRate(any(), any()) }
            assert(
                networkCurrenciesExchangeRate ==
                    Result.success(
                        CurrencyExchangeRate(
                            rates = exchangeRates,
                            base = "USD",
                        ),
                    ),
            )
        }

    @Test
    fun `test fetch exchange rate failure use case`() =
        runTest(dispatcherRule.testDispatcher) {
            setFailureData()
            val networkData = currencyRepository.getCurrencyExchangeRate("ABC", "")
            assert(networkData.isFailure)
        }
}
