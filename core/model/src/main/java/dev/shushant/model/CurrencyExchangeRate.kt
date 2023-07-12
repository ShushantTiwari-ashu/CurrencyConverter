package dev.shushant.model

data class CurrencyExchangeRate(
    val base: String? = "",
    val disclaimer: String? = "",
    val license: String? = "",
    val rates: Map<String, Double>,
)
