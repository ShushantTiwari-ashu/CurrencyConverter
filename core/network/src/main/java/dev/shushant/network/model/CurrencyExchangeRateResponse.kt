package dev.shushant.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyExchangeRateResponse(
    val base: String? = "USD",
    val disclaimer: String? = "",
    val license: String? = "",
    val rates: Map<CurrencyCode, Double>,
    val timestamp: Int? = 0,
)
