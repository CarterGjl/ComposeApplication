package com.example.composeapplication

import android.annotation.SuppressLint
import androidx.navigation.NavController

@SuppressLint("StaticFieldLeak")
object AppRuntime {
    var navController: NavController? = null
}