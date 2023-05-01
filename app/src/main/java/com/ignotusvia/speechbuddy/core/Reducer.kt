package com.ignotusvia.speechbuddy.core

/**
 * A reducer (aka intent in MVI) that is used by a [ModelStore] to emit a new state of its model.
 */
abstract class Reducer<S> {

    /**
     * Returns the new state to be emitted by the [ModelStore].
     *
     * @param oldState the current state of the model in the store
     * @return the new state of the model in the store. Should not be the same object.
     */
    abstract fun reduce(oldState: S): S
}

/**
 * Create an anonymous [Reducer] from a lambda.
 *
 * @param transformer the [reduce] function for this reducer.
 */
inline fun <S> reducer(crossinline transformer: (oldState: S) -> S) = object : Reducer<S>() {
    override fun reduce(oldState: S): S = transformer(oldState)
}