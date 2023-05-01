package com.ignotusvia.speechbuddy.core

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * This class checks the state of the user's internet connectivity before all calls go out and
 * throws more informative exceptions to give users a more useful error message if anything goes
 * wrong.
 */
class NetworkConnectivityInterceptor @Inject constructor(private val networkConnectivityManager: NetworkConnectivityManager) :
    Interceptor {

    @JsonClass(generateAdapter = false)
    enum class NetworkState {
        /**
         * User has no Wi-Fi, mobile, or any other network turned on or functional for their phone
         * settings
         */
        @Json(name = "NO_AVAILABLE_NETWORKS")
        NO_AVAILABLE_NETWORKS,

        /**
         * User has networks turned on but can't access them for some reason (network deadzone,
         * etc.)
         */
        @Json(name = "NO_EXTERNAL_NET_ACCESS")
        NO_EXTERNAL_NET_ACCESS,

        /**
         * User is clear to make network requests!
         */
        @Json(name = "CONNECTED")
        CONNECTED,

        /**
         * Something else unexpected happened in this whole process (could be that the
         * ConnectivityManager was null or that getActiveNetworkInfo() failed somehow)
         */
        @Json(name = "UNKNOWN")
        UNKNOWN
    }

    /**
     * This method is responsible for checking if the user is even connected to the internet - if
     * they are, let the network request attempt to proceed.  If not, abort the request, and throw
     * the appropriate exception.
     *
     * @param chain The object holding the call waiting to go out
     * @return The response from this call
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        return when (networkConnectivityManager.checkConnectivity()) {
            NetworkState.CONNECTED -> chain.proceed(chain.request())
            NetworkState.NO_AVAILABLE_NETWORKS -> throw NoAvailableNetworksException("User has no Wi-Fi, mobile, or other network turned on or available")
            NetworkState.NO_EXTERNAL_NET_ACCESS -> throw NoExternalNetAccessException("User has networks on but no connectivity")
            NetworkState.UNKNOWN -> throw UnknownNetworkError("No ConnectivityManager found or other error in determining network state")
        }
    }
}
