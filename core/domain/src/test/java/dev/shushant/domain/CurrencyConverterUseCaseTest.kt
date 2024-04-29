package dev.shushant.domain

import dev.shushant.data.repository.CurrencyRepository
import dev.shushant.model.Currencies
import dev.shushant.model.CurrencyExchangeRate
import dev.shushant.test.MainDispatcherRule
import dev.shushant.test.currencies
import dev.shushant.test.exchangeRates
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CurrencyConverterUseCaseTest {
    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule(order = 2)
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var repository: CurrencyRepository

    private lateinit var useCase: CurrencyConverterUseCase

    @Before
    fun setup() {
        coEvery { repository.getCurrencies() } returns
            Result.success(
                Currencies(
                    items =
                        currencies.map {
                            Currencies.Item(
                                currencyCode = it.key,
                                currencyName = it.value,
                            )
                        },
                ),
            )
        coEvery {
            repository.getCurrencyExchangeRate(
                any(), any(),
            )
        } returns Result.success(CurrencyExchangeRate(rates = exchangeRates))
        useCase = CurrencyConverterUseCase(repository)
    }

    @Test
    fun `test currencies`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val currencies = useCase.invoke()
            assert(currencies.isSuccess)
        }

    @Test
    fun `test exchangeRates`() =
        runTest(mainDispatcherRule.testDispatcher) {
            val exchangeRate = useCase.invoke("USD", "")
            assert(exchangeRate.isSuccess)
        }
}
