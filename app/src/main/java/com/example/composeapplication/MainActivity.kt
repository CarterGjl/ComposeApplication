package com.example.composeapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeapplication.learn.Title
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.timer.TimerViewModel
import com.example.composeapplication.viewmodel.MovieViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val viewModel: TimerViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets {
                ComposeApplicationTheme {
                    ArticleScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.animatorController.stop()
    }

}
@Composable
fun ExpandingCard(title: String, body: String) {
    var expanded by remember { mutableStateOf(false) }

    // describe the card for the current state of expanded
    Card(modifier = Modifier.background(color = Color.Gray).fillMaxWidth().wrapContentHeight().padding(top = 8.dp)) {
        Column(
            Modifier
                .width(280.dp)
                .animateContentSize() // automatically animate size when it changes
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(text = title)

            // content of the card depends on the current value of expanded
            if (expanded) {
                Text(text = body, Modifier.padding(top = 8.dp))
                // change expanded in response to click events
                IconButton(onClick = { expanded = false }, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.ExpandLess, contentDescription = "Expand less")
                }
            } else {
                // change expanded in response to click events
                IconButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.ExpandMore, contentDescription = "Expand more")
                }
            }
        }
    }
}
@Composable
fun PasswordTextField() {
    val movieViewModel: MovieViewModel = viewModel()
    var password by remember { mutableStateOf("") }
    movieViewModel.movies
    TextField(
        value = password,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = {
            password = it
            movieViewModel.getArticles(0)
        },
        label = { Text("Enter password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun ArticleScreen() {
    Scaffold(topBar = {
        Column {
            Spacer(
                modifier = Modifier
                    .statusBarsHeight()
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primaryVariant)
            )
            TopAppBar(
                title = {
                    Text(text = "compose demo")
                },
                backgroundColor = MaterialTheme.colors.primaryVariant,
                elevation = 0.dp
            )
        }

    }) {
        val movieViewModel: MovieViewModel = viewModel()
        Column(Modifier.fillMaxHeight().fillMaxWidth().background(color = Color.Gray)) {
            movieViewModel.getArticles(0)
            val result = movieViewModel.movies.observeAsState()
            if (result.value != null){
                LazyColumn(contentPadding = PaddingValues(8.dp)) {
                    if (result.value != null) {
                        val datas = result.value!!.data.datas
                        items(datas.size) { index ->
//                            Title(article = datas[index])
                            ExpandingCard(title = datas[index].title, body = datas[index].desc)
                        }
                    }

                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(color = Color.Gray)
                ) {
                    Text(text = "empty")
                }
            }
        }

    }
}

@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun BoxScope() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(text = "empty")
    }
}