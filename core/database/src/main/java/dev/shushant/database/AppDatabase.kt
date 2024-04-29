package dev.shushant.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.shushant.database.converter.MapTypeConverter
import dev.shushant.database.dao.CurrencyDao
import dev.shushant.database.dao.CurrencyExchangeRatesDao
import dev.shushant.database.entity.CurrenciesEntity
import dev.shushant.database.entity.CurrenciesExchangeRateEntity

@Database(
    entities = [CurrenciesEntity::class, CurrenciesExchangeRateEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    MapTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    abstract fun currencyExchangeDao(): CurrencyExchangeRatesDao
}
