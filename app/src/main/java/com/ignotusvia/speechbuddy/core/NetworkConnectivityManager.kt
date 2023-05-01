package com.ignotusvia.speechbuddy.core

import android.net.ConnectivityManager
import androidx.annotation.NonNull
import javax.inject.Inject

class NetworkConnectivityManager @Inject constructor(private val connMgr: ConnectivityManager?) {

    @Suppress("DEPRECATION")
    @NonNull
    fun checkConnectivity(): NetworkConnectivityInterceptor.NetworkState {
        if (connMgr == null) return NetworkConnectivityInterceptor.NetworkState.UNKNOWN

        val networkInfo = connMgr.activeNetworkInfo

        return when {
            networkInfo == null -> NetworkConnectivityInterceptor.NetworkState.NO_AVAILABLE_NETWORKS
            !networkInfo.isConnected -> NetworkConnectivityInterceptor.NetworkState.NO_EXTERNAL_NET_ACCESS
            else -> NetworkConnectivityInterceptor.NetworkState.CONNECTED
        }
    }

}