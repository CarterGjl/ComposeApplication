@file:Suppress("unused", "LiftReturnOrAssignment", "NullableBooleanElvis")

package com.example.composeapplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

private val PATTERN = longArrayOf(200, 150)

object Utils {

    private const val TAG_DEBUG = "ComposeMovie.Debug"
    const val TAG_LAUNCH = "ComposeMovie.Launch"
    const val TAG_SEARCH = "ComposeMovie.Search"
    const val TAG_NETWORK = "ComposeMovie.Network"

    fun logError(tag: String?, message: String?, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }

    fun logDebug(tag: String, message: String, throwable: Throwable? = null) {
        if (Log.isLoggable(TAG_DEBUG, Log.DEBUG)) {
            if (throwable != null) Log.d(tag, message, throwable)
            else Log.d(tag, message)
        }
    }

    fun ensureNetworkAvailable(context: Context, needToast: Boolean = true): Boolean {
        val isAvailable = isNetworkAvailable(context)
        if (!isAvailable && needToast) Toast.makeText(
            context, R.string.search_failure, Toast.LENGTH_SHORT
        ).show()
        return isAvailable
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun Context.vibrateOnce() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            tryVibrate(vibratorManager, VibrationEffect.Composition.PRIMITIVE_LOW_TICK)
        } else {
            @Suppress("DEPRECATION")
            val defaultVibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            defaultVibrator.vibrate(
                effect
            )
        }

    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun isPrimitiveSupported(vibratorManager: VibratorManager, effectId: Int): Boolean {
        return vibratorManager.defaultVibrator.areAllPrimitivesSupported(effectId)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun tryVibrate(vibratorManager: VibratorManager, effectId: Int) {
        if (isPrimitiveSupported(vibratorManager, effectId)) {
            vibratorManager.vibrate(
                CombinedVibration.createParallel(
                    VibrationEffect.startComposition().addPrimitive(effectId).compose()
                )
            )
        }

    }
}