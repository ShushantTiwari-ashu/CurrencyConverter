package dev.shushant.data.repository

import dev.shushant.data.model.asEntity
import dev.shushant.data.utils.AppDispatcher
import dev.shushant.data.utils.isTimestamp30MinutesAhead
import dev.shushant.database.entity.toCurrencies
import dev.shushant.database.entity.toCurrencyExchangeRate
import dev.shushant.database.model.LocalDataSource
import dev.shushant.model.Currencies
import dev.shushant.model.CurrencyExchangeRate
import dev.shushant.network.source.NetworkDataSource
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcher: AppDispatcher
) : CurrencyRepository {

    override suspend fun getCurrencies(): Result<Currencies> =
        withContext(dispatcher.dispatcherIO) {
            localDataSource.currencyDao.getCurrencies().let { currencies ->
                if (currencies == null) {
                    fetchCurrenciesFromNetworkAndSaveToDatabase()
                } else if (Instant.fromEpochMilliseconds(currencies.timeStamp)
                        .isTimestamp30MinutesAhead()
                ) {
                    fetchCurrenciesFromNetworkAndSaveToDatabase()
                } else {
                    runCatching {
                        Result.success(localDataSource.currencyDao.getCurrencies()!!.toCurrencies())
                    }.getOrElse { Result.failure(it) }
                }
            }
        }

    private suspend fun fetchCurrenciesFromNetworkAndSaveToDatabase(): Result<Currencies> {
        val fetchedCurrencies = networkDataSource.getCurrencies()
        return when {
            (fetchedCurrencies.isSuccess) -> {
                fetchedCurrencies.getOrNull()?.asEntity()
                    ?.let { localDataSource.currencyDao.insertCurrencies(it) }
                Result.success(localDataSource.currencyDao.getCurrencies()!!.toCurrencies())
            }

            else -> {
                Result.failure(fetchedCurrencies.exceptionOrNull() ?: Throwable("Unknown Error!"))
            }
        }
    }

    override suspend fun getCurrencyExchangeRate(
        base: String, symbols: String
    ): Result<CurrencyExchangeRate> = withContext(dispatcher.dispatcherIO) {
        localDataSource.currencyExchangeRatesDao.getExchangeRates().let { exchangeRates ->
            if (exchangeRates == null) {
                fetchCurrencyExchangeRateFromNetworkAndSaveToDatabase(base, symbols)
            } else if (Instant.fromEpochMilliseconds(exchangeRates.timestamp)
                    .isTimestamp30MinutesAhead()
            ) {
                fetchCurrencyExchangeRateFromNetworkAndSaveToDatabase(base, symbols)
            } else {
                runCatching {
                    Result.success(
                        localDataSource.currencyExchangeRatesDao.getExchangeRates()!!
                            .toCurrencyExchangeRate()
                    )
                }.getOrElse {
                    Result.failure(it)
                }
            }

        }
    }

    private suspend fun fetchCurrencyExchangeRateFromNetworkAndSaveToDatabase(
        base: String, symbols: String
    ): Result<CurrencyExchangeRate> {
        val fetchExchangeRates = networkDataSource.getCurrencyExchangeRate(base, symbols)
        return when {
            fetchExchangeRates.isSuccess -> {
                fetchExchangeRates.getOrNull()?.asEntity()
                    ?.let { localDataSource.currencyExchangeRatesDao.insertExchangeRates(it) }
                Result.success(
                    localDataSource.currencyExchangeRatesDao.getExchangeRates()!!
                        .toCurrencyExchangeRate()
                )
            }

            else -> {
                Result.failure(fetchExchangeRates.exceptionOrNull() ?: Throwable("Unknown Error!"))
            }
        }
    }

}