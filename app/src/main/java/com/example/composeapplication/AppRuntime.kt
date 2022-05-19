package com.example.composeapplication

import android.annotation.SuppressLint
import androidx.compose.material.ScaffoldState
import androidx.navigation.NavController

@SuppressLint("StaticFieldLeak")
object AppRuntime {
    var rememberScaffoldState: ScaffoldState? = null
}