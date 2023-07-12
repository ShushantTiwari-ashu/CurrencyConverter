package dev.shushant.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyExchangeRateResponse(
    val base: String? = "USD", // USD
    val disclaimer: String? = "", // Usage subject to terms: https://openexchangerates.org/terms
    val license: String? = "", // https://openexchangerates.org/license
    val rates: Map<CurrencyCode, Double>,
    val timestamp: Int? = 0 // 1688378400,
)