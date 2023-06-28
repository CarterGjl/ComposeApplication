package com.example.composeapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.SubcomposeAsyncImage
import com.example.composeapplication.R
import com.example.composeapplication.Utils

@Composable
fun LoadImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderColor: Color? = MaterialTheme.colors.compositedOnSurface(0.2f)
) {
    SubcomposeAsyncImage(
        model = url,
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = contentScale,
        onLoading = {
            Utils.logDebug(Utils.TAG_NETWORK, "Image loading")
        },
        onError = {
            Utils.logDebug(
                Utils.TAG_NETWORK,
                "Image error msg:${it.result.throwable.message}"/*, it.throwable*/
            )
        },
        onSuccess = {
            Utils.logDebug(
                Utils.TAG_NETWORK,
                "Image succeed with source:${it.result}"
            )
        },
        error = {
            Image(painterResource(R.drawable.ic_error), contentDescription = "Error")
        },
        loading = {
            if (placeholderColor != null) {
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(placeholderColor)
                )
            }
        }
    )
}