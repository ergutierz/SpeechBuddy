package com.ignotusvia.speechbuddy.core

class ConsumableEvent<T> {
    private var consumed = false

    var value: T? = null
        set(value) {
            consumed = false
            field = value
        }
        get() {
            if (consumed) {
                return null
            }
            consumed = true
            return field
        }

    companion object {
        /**
         * Creates a new instance of UIEvent with the given initial value.
         *
         * @param t the initial value for the UIEvent
         * @return a new UIEvent instance with the initial value
         */
        fun <T> create(t: T): ConsumableEvent<T> {
            return ConsumableEvent<T>().apply {
                value = t
            }
        }
    }
}

inline fun <T> ConsumableEvent<T>.handleEvent(action: (T) -> Unit) {
    value?.let { event ->
        action(event)
    }
}