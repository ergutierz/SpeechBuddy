package com.ignotusvia.speechbuddy.core

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow

interface ModelStore<S> {
    /**
     * Enqueues the [reducer] to be processed on the latest [modelState].
     * Returns [StateFlow] of the [Reducer] created that will emit once the reducer has been processed.
     */
    suspend fun process(reducer: Reducer<S>): StateFlow<S>

    /**
     * Enqueues the [reducer] to be processed on the latest [modelState]. Returns [StateFlow] of the
     * [Reducer] created that will emit once the reducer has been processed.
     *
     * @see process
     * @param transformer a method that takes the initial state, and returns the new state.
     */
    suspend fun process(transformer: (S) -> S) = process(reducer(transformer))

    /**
     * A [Flow] emitting the current state of the model store, beginning with the latest
     * processed state.
     */
    fun modelState(): Flow<S>

    /**
     * The current value of the model store.
     */
    val value: S
}