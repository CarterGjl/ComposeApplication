package com.example.composeapplication

import android.annotation.SuppressLint
import androidx.compose.material3.SnackbarHostState

@SuppressLint("StaticFieldLeak")
object AppRuntime {
    var rememberScaffoldState: SnackbarHostState? = null
}