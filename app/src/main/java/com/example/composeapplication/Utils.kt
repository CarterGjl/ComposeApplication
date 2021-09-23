@file:Suppress("unused", "LiftReturnOrAssignment", "NullableBooleanElvis")

package com.example.composeapplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import android.net.wifi.WifiInfo

import android.annotation.SuppressLint

import android.net.wifi.WifiManager




object Utils {

    // adb shell setprop log.tag.ComposeMovie.Debug D
    private const val TAG_DEBUG = "ComposeMovie.Debug"
    const val TAG_LAUNCH = "ComposeMovie.Launch"
    const val TAG_SEARCH = "ComposeMovie.Search"
    const val TAG_NETWORK = "ComposeMovie.Network"

    fun logError(tag: String?, message: String?, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }

    fun logDebug(tag: String, message: String, throwable: Throwable? = null) {
        if (Log.isLoggable(TAG_DEBUG, Log.DEBUG)) {
            if (throwable != null)
                Log.d(tag, message, throwable)
            else
                Log.d(tag, message)
        }
    }

//    private fun isNetworkAvailable(context: Context): Int {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo = connectivityManager.allNetworkInfo
//        if (networkInfo.isNotEmpty()) {
//            for (i in networkInfo.indices) {
//                if (networkInfo[i].state == NetworkInfo.State.CONNECTED) {
//                    return 1
//                }
//            }
//        }
//        return 0
//    }

    fun ensureNetworkAvailable(context: Context, needToast: Boolean = true): Boolean {
        val isAvailable = isNetworkAvailable(context)
        if (!isAvailable && needToast) Toast.makeText(
            context,
            R.string.search_failure,
            Toast.LENGTH_SHORT
        )
            .show()
        return isAvailable
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}