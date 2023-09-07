package com.example.composeapplication.ui.widget

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(name = "ViewfinderView")
@Composable
fun ViewfinderView() {
    Scanner()
}

private const val TAG = "ViewfinderView"

@Composable
fun Scanner() {

    var topValue by remember {
        mutableFloatStateOf(500F)
    }
    var frame by remember {
        mutableStateOf(Rect(0F, 0F, 0F, 0F))
    }

    Log.d(TAG, "ViewfinderView: $topValue")
    var width = 0F
    var height = 0F
    var boardSize = 0F
    val rememberCoroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        rememberCoroutineScope.launch {
            repeat(Int.MAX_VALUE) {
                delay(20)
                if (height == 0F) {
                    return@repeat
                }
                if (boardSize == 0F) {
                    boardSize = width.coerceAtMost(height) * 0.625F
                    val leftOffsets = (width - boardSize) / 2f
                    val topOffsets = (height - boardSize) / 2f
                    frame = Rect(
                        leftOffsets, topOffsets, leftOffsets + boardSize,
                        topOffsets + boardSize
                    )
                }
                if (topValue > height - 500) {
                    topValue = 500F
                } else {
                    topValue += 2
                }
            }
        }
    }
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Creating a Radial Gradient Color
        val gradientRadial = Brush.horizontalGradient(
            listOf(Color(0xFF1FB3E2), Color(0xFF1FB3E2)), tileMode = TileMode.Mirror
        )
        width = size.width
        height = size.height
        drawOval(
            gradientRadial,
            topLeft = Offset(100F, topValue),
            size = Size(width - 200, 5F)
        )

    }
}