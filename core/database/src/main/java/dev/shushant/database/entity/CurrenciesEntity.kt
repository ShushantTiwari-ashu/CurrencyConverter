package dev.shushant.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.shushant.database.entity.CurrenciesEntity.Companion.TABLE_NAME
import dev.shushant.model.Currencies
import kotlinx.datetime.Clock

@Entity(
    tableName = TABLE_NAME,
)
data class CurrenciesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val currencies: Map<String, String>,
    val timeStamp: Long = Clock.System.now().toEpochMilliseconds(),
) {
    companion object {
        const val TABLE_NAME = "CURRENCIES"
    }
}

fun CurrenciesEntity.toCurrencies() =
    Currencies(
        items =
            currencies.map {
                Currencies.Item(it.key, it.value)
            },
    )
