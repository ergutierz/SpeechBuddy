package com.ignotusvia.speechbuddy.core

import java.io.IOException

class NoAvailableNetworksException : IOException {
    @Suppress("unused")
    constructor() : super()

    @Suppress("unused")
    constructor(message: String, cause: Throwable?) : super(message, cause)

    @Suppress("unused")
    constructor(cause: Throwable?) : super(cause)

    @Suppress("unused")
    constructor(message: String) : super(message)
}

/**
 * The user has Wi-Fi, Mobile, or another network turned on but are somehow unable to use it (in a
 * deadzone, etc.).
 */
class NoExternalNetAccessException : IOException {
    @Suppress("unused")
    constructor() : super()

    @Suppress("unused")
    constructor(message: String, cause: Throwable?) : super(message, cause)

    @Suppress("unused")
    constructor(cause: Throwable?) : super(cause)

    @Suppress("unused")
    constructor(message: String) : super(message)
}

/**
 * This exception is thrown because something else unexpected happened when trying to get information
 * about the user's network access on their phone (from Context.getSystemService)
 *
 */
class UnknownNetworkError : IOException {
    @Suppress("unused")
    constructor() : super()

    @Suppress("unused")
    constructor(message: String, cause: Throwable?) : super(message, cause)

    @Suppress("unused")
    constructor(cause: Throwable?) : super(cause)

    @Suppress("unused")
    constructor(message: String) : super(message)
}