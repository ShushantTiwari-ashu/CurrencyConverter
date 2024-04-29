package dev.shushant.data.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<STATE : State>(initialState: STATE) : ViewModel(), CoroutineScope {
    /**
     * Mutable State of this ViewModel
     */
    private val _state = MutableStateFlow(initialState)

    private val parentJob = Job()
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

    override val coroutineContext: CoroutineContext
        get() = parentJob + coroutineExceptionHandler + Dispatchers.Main

    protected val viewModelScope: CoroutineScope
        get() = CoroutineScope(coroutineContext)

    /**
     * State to be exposed to the UI layer
     */
    val state: StateFlow<STATE> = _state.asStateFlow()

    /**
     * Retrieves the current UI state
     */
    val currentState: STATE get() = state.value

    /**
     * Updates the state of this ViewModel and returns the new state
     *
     * @param update Lambda callback with old state to calculate a new state
     * @return The updated state
     */
    protected fun setState(update: (old: STATE) -> STATE): STATE = _state.updateAndGet(update)
}
