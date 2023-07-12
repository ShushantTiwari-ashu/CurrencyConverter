package dev.shushant.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shushant.database.AppDatabase
import dev.shushant.database.dao.CurrencyDao
import dev.shushant.database.dao.CurrencyExchangeRatesDao
import dev.shushant.database.model.LocalDataSource

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesCurrencyDao(
        database: AppDatabase,
    ): CurrencyDao = database.currencyDao()

    @Provides
    fun providesCurrencyExchangeDao(
        database: AppDatabase,
    ): CurrencyExchangeRatesDao = database.currencyExchangeDao()

    @Provides
    fun providesLocalDataSource(
        currencyDao: CurrencyDao,
        currencyExchangeRatesDao: CurrencyExchangeRatesDao
    ) = LocalDataSource(currencyDao, currencyExchangeRatesDao)
}
