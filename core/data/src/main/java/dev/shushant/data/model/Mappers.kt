package dev.shushant.data.model

import dev.shushant.database.entity.CurrenciesEntity
import dev.shushant.database.entity.CurrenciesExchangeRateEntity
import dev.shushant.network.model.CurrenciesResponse
import dev.shushant.network.model.CurrencyExchangeRateResponse

fun CurrenciesResponse.asEntity() = CurrenciesEntity(
    currencies = data
)

fun CurrencyExchangeRateResponse.asEntity() = CurrenciesExchangeRateEntity(
    base = base,
    disclaimer = disclaimer,
    license = license,
    rates = rates
)