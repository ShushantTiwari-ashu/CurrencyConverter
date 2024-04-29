package dev.shushant.domain

import dev.shushant.data.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyConverterUseCase
    @Inject
    constructor(private val currencyRepository: CurrencyRepository) {
        suspend operator fun invoke() = currencyRepository.getCurrencies()

        suspend operator fun invoke(
            base: String,
            symbols: String,
        ) = currencyRepository.getCurrencyExchangeRate(base, symbols)
    }
