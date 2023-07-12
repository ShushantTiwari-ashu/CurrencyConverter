package dev.shushant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import dev.shushant.database.entity.CurrenciesExchangeRateEntity
import dev.shushant.database.entity.CurrenciesExchangeRateEntity.Companion.TABLE_NAME

@Dao
interface CurrencyExchangeRatesDao {

    @Insert
    suspend fun insert(rates: CurrenciesExchangeRateEntity): Long

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteRecord()

    @Query("SELECT * FROM $TABLE_NAME")
    fun getExchangeRates(): CurrenciesExchangeRateEntity?

    @Transaction
    suspend fun insertExchangeRates(rates: CurrenciesExchangeRateEntity) {
        deleteRecord()
        insert(rates)
    }

}