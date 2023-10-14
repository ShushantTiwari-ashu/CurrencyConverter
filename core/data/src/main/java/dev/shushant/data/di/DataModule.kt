package dev.shushant.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shushant.data.repository.CurrencyRepository
import dev.shushant.data.repository.CurrencyRepositoryImpl
import dev.shushant.data.utils.AppDispatcher
import dev.shushant.data.utils.AppDispatcherImpl
import dev.shushant.data.utils.ConnectivityManagerNetworkMonitor
import dev.shushant.data.utils.NetworkMonitor

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsCurrencyRepository(
        currencyRepository: CurrencyRepositoryImpl
    ): CurrencyRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor

    @Binds
    fun bindsDispatcher(appDispatcherImpl: AppDispatcherImpl): AppDispatcher
}