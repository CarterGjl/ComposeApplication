package com.example.composeapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.compose.*
import com.example.composeapplication.activity.bsae.BaseActivity

class PhotoActivity : BaseActivity() {

    companion object {
        const val PHOTO_URL = "photo_url"
        fun go(context: Activity, url: String) {
            with(Intent(context, PhotoActivity::class.java)) {
                putExtra(PHOTO_URL, url)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(PHOTO_URL)
        setContent {
            PhotoViewPage(url = url!!) {
                finish()
            }
        }
    }
}

@Composable
fun PhotoViewPage(url: String, click: () -> Unit) {
    var scale by remember {
        mutableStateOf(1f)
    }
    val rotation by remember {
        mutableStateOf(0f)
    }
    val state = rememberTransformableState{
            zoomChange, _, _ ->
        scale *= zoomChange
        if (scale >= 6.0) scale = 6.0f
        else if (scale <= 1.0) scale = 1.0f
    }
    val painter = rememberAsyncImagePainter(url)
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Image(
            painter = painter,
            contentDescription = "picture",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation
                )
                .transformable(state = state)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                        },
                    )
                }
        )
        if (painter.state is  AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        BackArrowDown(click = click)
    }
}

@Composable
fun BackArrowDown(click: () -> Unit) {
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .padding(15.dp, 35.dp, 0.dp, 0.dp)
            .size(24.dp),
        color = Color.Gray
    ) {
        Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.clickable {
                click()
            })
    }
}

@Composable
fun DropdownDemo() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("A", "B", "C", "D", "E", "F")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopEnd),contentAlignment = Alignment.CenterEnd) {
        Text(items[selectedIndex],modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true }).background(
            Color.Gray))
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(100.dp).background(
                Color.Red)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                }) {
                    val disabledText = if (s == disabledValue) {
                        " (Disabled)"
                    } else {
                        ""
                    }
                    Text(text = s + disabledText)
                }
            }
        }
    }
}
