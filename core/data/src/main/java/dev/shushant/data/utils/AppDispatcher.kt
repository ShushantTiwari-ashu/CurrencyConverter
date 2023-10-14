package dev.shushant.data.utils

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatcher {
    val dispatcherIO: CoroutineDispatcher
    val dispatcherMain: CoroutineDispatcher
}