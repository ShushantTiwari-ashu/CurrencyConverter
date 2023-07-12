package dev.shushant.network.source

import dev.shushant.network.model.CurrenciesResponse
import dev.shushant.network.model.CurrencyExchangeRateResponse
import dev.shushant.network.service.CurrencyConverterNetworkApi
import dev.shushant.test.MainDispatcherRule
import dev.shushant.test.currencies
import dev.shushant.test.exchangeRates
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class NetworkDataSourceTest {
    @get: Rule(order = 1)
    val dispatcherRule = MainDispatcherRule()

    @get: Rule(order = 2)
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var service: CurrencyConverterNetworkApi

    private val json: Json = Json { ignoreUnknownKeys = true }

    private lateinit var networkDataSource: NetworkDataSource

    @Before
    fun setup() {
        networkDataSource = NetworkDataSourceImpl(service, json)
    }

    @Test
    fun `test currencies api success`() = runTest(dispatcherRule.testDispatcher) {
        val obj = json.encodeToJsonElement(json.encodeToJsonElement(currencies)) as JsonObject
        coEvery { service.getCurrencies() } returns Response.success(obj)
        assert(networkDataSource.getCurrencies() == Result.success(CurrenciesResponse(data = currencies)))
    }

    @Test
    fun `test currencies api error`() = runTest(dispatcherRule.testDispatcher) {
        coEvery { service.getCurrencies() } returns Response.error(
            500,
            "".toResponseBody()
        )
        assert(
            networkDataSource.getCurrencies().isFailure
        )
    }

    @Test
    fun `test getCurrencyExchangeRate api success`() = runTest(dispatcherRule.testDispatcher) {
        val actual = CurrencyExchangeRateResponse(rates = exchangeRates)
        coEvery { service.getCurrencyRates(any(), any()) } returns Response.success(
            actual
        )
        assert(
            networkDataSource.getCurrencyExchangeRate("USD", "") == Result.success(
                actual
            )
        )
    }

    @Test
    fun `test getCurrencyExchangeRate api error`() = runTest(dispatcherRule.testDispatcher) {
        coEvery { service.getCurrencyRates(any(), any()) } returns Response.error(
            500,
            "".toResponseBody()
        )
        assert(
            networkDataSource.getCurrencyExchangeRate("USD", "").isFailure
        )
    }
}