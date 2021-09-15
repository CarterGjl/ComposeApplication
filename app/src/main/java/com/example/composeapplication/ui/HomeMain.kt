package com.example.composeapplication.ui

import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeMain() {
    val state = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = state,
        floatingActionButton = {
            Button(onClick = {
                scope.launch {
                    state.snackbarHostState.showSnackbar("hello")
                }
            }) {
                Text(text = "click")
            }
        }
    ) {

    }
}