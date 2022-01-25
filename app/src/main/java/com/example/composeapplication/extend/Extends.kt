package com.example.composeapplication.extend

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun String.parseHighlight(): AnnotatedString {
    val parts = this.split("<em class='highlight'>", "</em>")
    return buildAnnotatedString {
        var highlight = false
        for (part in parts) {
            if (highlight) {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(part)
                }
            } else {
                append(part)
            }
            highlight = !highlight
        }
    }
}