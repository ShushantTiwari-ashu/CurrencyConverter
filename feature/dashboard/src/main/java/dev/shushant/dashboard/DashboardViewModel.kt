package dev.shushant.dashboard

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shushant.data.base.BaseViewModel
import dev.shushant.data.base.State
import dev.shushant.data.utils.NetworkMonitor
import dev.shushant.domain.CurrencyConverterUseCase
import dev.shushant.model.Currencies
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DashboardViewModel
    @Inject
    constructor(
        private val currencyConverterUseCase: CurrencyConverterUseCase,
        networkMonitor: NetworkMonitor,
    ) : BaseViewModel<DashboardUIState>(DashboardUIState()) {
        private val isOnline =
            networkMonitor.isOnline.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = true,
            )

        init {
            observeNetworkConnectivity()
        }

        private fun observeNetworkConnectivity() {
            viewModelScope.launch {
                isOnline.collect {
                    if (it && currentState.currencies == null) {
                        fetchCurrencies()
                    }
                }
            }
        }

        private fun fetchCurrencies() {
            viewModelScope.launch {
                setState { state -> state.copy(isLoading = true) }
                val result = currencyConverterUseCase.invoke()
                when {
                    result.isFailure -> {
                        setState { state ->
                            state.copy(
                                error = result.exceptionOrNull()?.localizedMessage ?: "",
                                isLoading = false,
                            )
                        }
                    }

                    result.isSuccess ->
                        setState { state ->
                            state.copy(
                                currencies = result.getOrNull(),
                                isLoading = false,
                                error = null,
                            )
                        }
                }
            }
        }

        private fun fetchExchangeRates(
            baseCurrency: String,
            amount: BigDecimal,
        ) {
            viewModelScope.launch {
                setState { state -> state.copy(isLoading = true) }
                val result =
                    currencyConverterUseCase.invoke(
                        base = BASE_C,
                        symbols =
                            currentState.currencies?.items?.joinToString(separator = ",") { it.currencyCode }
                                ?: "",
                    )
                when {
                    result.isFailure -> {
                        setState { state ->
                            state.copy(
                                error = result.exceptionOrNull()?.localizedMessage ?: "",
                                isLoading = false,
                            )
                        }
                    }

                    result.isSuccess ->
                        setState { state ->
                            state.copy(
                                exchangeRates = result.getOrNull()?.rates ?: emptyMap(),
                                isLoading = false,
                                error = null,
                            )
                        }.also {
                            convertCurrency(baseCurrency, amount)
                        }
                }
            }
        }

        fun updateBaseCurrency(
            baseCurrency: String,
            amount: String,
        ) {
            amount.takeIf { it.isNotBlank() }?.let {
                fetchExchangeRates(baseCurrency, amount.toBigDecimal())
            } ?: run {
                setState { state -> state.copy(convertedAmount = emptyMap()) }
            }
        }

        companion object {
            const val BASE_C = "USD"
        }

        private fun convertCurrency(
            baseCurrency: String,
            sourceAmount: BigDecimal,
        ) {
            val mapOfExchangeRate = mutableMapOf<String, Double>()
            val usdEquivalent =
                sourceAmount.toDouble().div(
                    currentState.exchangeRates.getOrDefault(baseCurrency, Random.nextDouble(0.0, 100.0)),
                )
            for (currency in currentState.exchangeRates) {
                val convertedAmount =
                    usdEquivalent * currentState.exchangeRates.getValue(currency.key)
                mapOfExchangeRate[currency.key] = convertedAmount
            }
            setState { state ->
                state.copy(convertedAmount = mapOfExchangeRate)
            }
        }
    }

data class DashboardUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currencies: Currencies? = null,
    val exchangeRates: Map<String, Double> = emptyMap(),
    val convertedAmount: Map<String, Double> = emptyMap(),
) : State
