package dev.shushant.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import dev.shushant.database.entity.CurrenciesEntity
import dev.shushant.database.entity.CurrenciesEntity.Companion.TABLE_NAME

@Dao
interface CurrencyDao {
    @Insert
    suspend fun insert(currency: CurrenciesEntity): Long

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteRecord()

    @Query("SELECT * FROM $TABLE_NAME")
    fun getCurrencies(): CurrenciesEntity?

    @Transaction
    suspend fun insertCurrencies(currency: CurrenciesEntity) {
        deleteRecord()
        insert(currency)
    }
}
