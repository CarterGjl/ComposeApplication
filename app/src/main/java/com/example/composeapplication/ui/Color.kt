@file:Suppress("unused")

package com.example.composeapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

@Composable
fun Color.compositedOnSurface(alpha: Float): Color {
    return this.copy(alpha = alpha).compositeOver(this)
}

val purple200 = Color(0xFFBB86FC)
val purple500 = Color(0xFF6200EE)
val purple700 = Color(0xFF3700B3)
val teal200 = Color(0xFF03DAC5)

val toolBarColor = Color(0xFFFBDE4B)
// Color(0xFFcbc547)
val nameColor = Color(0xFF411445)
val itemCardColor = Color(0xFFf2eada)
val infoCardColor = itemCardColor
// Color(0xFFffce7b)
val infoButtonColor = nameColor
val infoColor = nameColor
// Color(0xFF72777b)
val likeColor = Color(0xFF888888)
val likedColor = nameColor
// Color(0xFFd4237a)
val likeColorBg = Color(0xFFf2eada)

//Color.kt
val pink100 = Color(0xFFFFF1F1)
val pink900 = Color(0xFF3f2c2c)
val gray = Color(0xFF232323)
val whit850 = Color.White.copy(alpha = .85f)
val whit150 = Color.White.copy(alpha = .15f)
val green900 = Color(0xFF2d3b2d)
val green300 = Color(0xFFb8c9b8)

val black = Color(0xFF1C1E28)
val darkBlue = Color(0xFF212E54)
val blue = Color(0xFF071C74)
val colorPrimaryDark1 = Color(0xFF303030)
val colorPrimaryDark2 = Color(0xFF232323)
val white = Color.White
val screenBGColor = darkBlue