package com.ignotusvia.speechbuddy.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * ModelStore dedicated to maintaining state, thread-safe, and handles buffer
 * to avoid race conditions
 * This state management mechanism takes in a reducer(intent) and updates the state
 */
open class StateFlowModelStore<S : Any>(
    startingState: S,
    coroutineScope: CoroutineScope
) : ModelStore<S>, CoroutineScope by coroutineScope {

    private val reducers = MutableSharedFlow<Reducer<S>>(extraBufferCapacity = 10)

    private val store: MutableStateFlow<S> = MutableStateFlow(startingState)

    private val processor = reducers
        .conflate()
        .scan(startingState) { oldState: S, reducer: Reducer<S> ->
            reducer.reduce(oldState)
        }
        .onEach { state: S ->
            withMutex {
                store.value = state
            }
        }
        .flowOn(Dispatchers.Default)

    private val mutex = Mutex()

    init {
        launch { processor.collect {} }
    }

    override suspend fun process(reducer: Reducer<S>): StateFlow<S> {
        reducers.emit(reducer)
        return modelState().take(1).stateIn(this)
    }

    override fun modelState(): Flow<S> = store.asStateFlow()

    override val value: S get() = store.value

    private suspend fun <R> withMutex(block: suspend () -> R): R {
        return mutex.withLock { block() }
    }
}
