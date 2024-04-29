package dev.shushant.dashboard

import dev.shushant.data.utils.NetworkMonitor
import dev.shushant.domain.CurrencyConverterUseCase
import dev.shushant.model.Currencies
import dev.shushant.model.CurrencyExchangeRate
import dev.shushant.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {
    @get:Rule(order = 1)
    val dispatcherRule = MainDispatcherRule()

    @get:Rule(order = 2)
    val mockkRule = MockKRule(this)

    private lateinit var viewModel: DashboardViewModel

    @MockK(relaxUnitFun = true)
    private lateinit var currencyConverterUseCase: CurrencyConverterUseCase

    @MockK(relaxUnitFun = true)
    private lateinit var networkMonitor: NetworkMonitor

    @Before
    fun setup() {
        coEvery { networkMonitor.isOnline } returns flowOf(true)
    }

    @Test
    fun `test fetchCurrencies success`() =
        runTest(dispatcherRule.testDispatcher) {
            setCurrencies()
            setNetworkCondition()
            setVM()
            assert(viewModel.currentState.currencies?.items?.first()?.currencyCode == "AED")
        }

    @Test
    fun `test fetchCurrencies error`() =
        runTest(dispatcherRule.testDispatcher) {
            coEvery { currencyConverterUseCase.invoke() } returns Result.failure(Throwable("No data found!"))
            setNetworkCondition()
            setVM()
            assert(viewModel.currentState.error == "No data found!")
        }

    @Test
    fun `test updateBaseCurrency and fetchExchangeRates success`() =
        runTest(dispatcherRule.testDispatcher) {
            setExchangeRateData()
            setNetworkCondition()
            setVM()
            viewModel.updateBaseCurrency("USD", amount = "123.00")
            assert(viewModel.currentState.exchangeRates == dev.shushant.test.exchangeRates)
        }

    private fun setExchangeRateData() {
        setCurrencies()
        coEvery {
            currencyConverterUseCase.invoke(
                any(), any(),
            )
        } returns Result.success(CurrencyExchangeRate(rates = dev.shushant.test.exchangeRates))
    }

    @Test
    fun `test updateBaseCurrency and fetchExchangeRates error`() =
        runTest(dispatcherRule.testDispatcher) {
            setCurrencies()
            coEvery {
                currencyConverterUseCase.invoke(
                    any(), any(),
                )
            } returns Result.failure(Throwable("No data found!"))
            setNetworkCondition()
            setVM()
            viewModel.updateBaseCurrency("USD", amount = "123.00")
            Assert.assertSame(
                viewModel.currentState.exchangeRates,
                emptyMap<String, Double>(),
            )
            assert(viewModel.currentState.error == "No data found!")
        }

    private fun setCurrencies() {
        coEvery { currencyConverterUseCase.invoke() } returns
            Result.success(
                Currencies(
                    items =
                        dev.shushant.test.currencies.map {
                            Currencies.Item(
                                currencyCode = it.key,
                                currencyName = it.value,
                            )
                        },
                ),
            )
    }

    @Test
    fun `test updateBaseCurrency and do not fetchExchangeRates when user has not entered the amount`() =
        runTest(dispatcherRule.testDispatcher) {
            setCurrencies()
            coEvery {
                currencyConverterUseCase.invoke(
                    any(), any(),
                )
            } returns Result.failure(Throwable("No data found!"))
            setNetworkCondition()
            setVM()
            viewModel.updateBaseCurrency("USD", amount = "")
            Assert.assertSame(
                viewModel.currentState.exchangeRates,
                emptyMap<String, Double>(),
            )
            assert(viewModel.currentState.exchangeRates == emptyMap<String, Double>())
        }

    @Test
    fun `convert USD to Ugandan Shilling(UGX)`() =
        runTest(dispatcherRule.testDispatcher) {
            setExchangeRateData()
            setNetworkCondition()
            setVM()
            viewModel.updateBaseCurrency("USD", "3")
            assert(viewModel.currentState.convertedAmount["UGX"] == 11071.650678)
        }

    @Test
    fun `convert UGX to Ukranian Hryvnia(UAH)`() =
        runTest(dispatcherRule.testDispatcher) {
            setExchangeRateData()
            setNetworkCondition()
            setVM()
            viewModel.updateBaseCurrency("UGX", "3")
            assert(viewModel.currentState.convertedAmount["UAH"] == 0.03002817201057651) {
                "${viewModel.currentState.convertedAmount["UAH"]}"
            }
        }

    @Test
    fun `convert ZMW to Unknown currency which is not present in exchange rate data`() =
        runTest(dispatcherRule.testDispatcher) {
            setExchangeRateData()
            setNetworkCondition()
            setVM()
            viewModel.updateBaseCurrency("ZMW", "3")
            assert(
                viewModel.currentState.convertedAmount.getOrDefault(
                    "UNKNOWN",
                    0.03002817201057651,
                ) == 0.03002817201057651,
            )
        }

    private fun setVM() {
        viewModel = DashboardViewModel(currencyConverterUseCase, networkMonitor)
    }

    private fun setNetworkCondition(isOnline: Boolean = true) {
        coEvery { networkMonitor.isOnline } returns flowOf(isOnline)
    }
}
