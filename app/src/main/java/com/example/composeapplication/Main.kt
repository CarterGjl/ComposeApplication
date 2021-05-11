package com.example.composeapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun Main(){
    Scaffold(
        topBar = {
            TopAppBar() {
                Text(text = "hello word")
            }
        },
    ) {
        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "for test")
    }
}