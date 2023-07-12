package dev.shushant.model

data class Currencies(val items: List<Item>) {
    data class Item(
        val currencyCode: String,
        val currencyName: String
    )
}
