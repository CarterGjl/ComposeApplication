package com.example.composeapplication.viewmodel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

class ComposeLayout {
}
@Preview
@Composable
fun FrameLayout() {
    Row(Modifier.background(Color.Blue)) {
        Button(onClick = {  }) {
            Text(text = "button")
        }
    }
}