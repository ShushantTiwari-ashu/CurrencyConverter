package dev.shushant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.shushant.database.entity.CurrenciesExchangeRateEntity.Companion.TABLE_NAME
import dev.shushant.model.CurrencyExchangeRate
import kotlinx.datetime.Clock

@Entity(
    tableName = TABLE_NAME,
)
data class CurrenciesExchangeRateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val base: String? = "",
    val disclaimer: String? = "",
    val license: String? = "",
    val rates: Map<String, Double>,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
) {
    companion object {
        const val TABLE_NAME = "EXCHANGE_RATE"
    }
}

fun CurrenciesExchangeRateEntity.toCurrencyExchangeRate() =
    CurrencyExchangeRate(
        base,
        disclaimer,
        license,
        rates,
    )
