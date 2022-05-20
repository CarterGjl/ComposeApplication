package com.example.composeapplication.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun ExpressLocationItem() {
    Scaffold(
        topBar = {
            TopAppBar {
                Text(text = "ello")
            }
        },

        ) {
        Row(
            Modifier
                .padding(it)
                .defaultMinSize(minHeight = 100.dp)
        ) {
            Text(text = "test")
            VerticalDivider(color = Color.Black)
        }
    }
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    val indentMod = if (startIndent.value != 0f) {
        Modifier.padding(start = startIndent)
    } else {
        Modifier
    }
    Box(
        modifier
            .then(indentMod)
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}