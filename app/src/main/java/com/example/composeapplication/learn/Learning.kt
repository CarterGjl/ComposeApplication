@file:Suppress("unused")

package com.example.composeapplication.learn

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.composeapplication.R
import com.example.composeapplication.bean.Article
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.timer.*
import com.example.composeapplication.viewmodel.ArticleViewModel

private const val TAG = "Learning"
@Preview(showBackground = true)
@Composable
fun NewsStory() {
    val image = painterResource(R.drawable.header)
    MaterialTheme {
        val typography = MaterialTheme.typography
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            val imageModifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clickable {
                    Log.d(TAG, "NewsStory: ")
                }
                .clip(shape = RoundedCornerShape(4.dp))
            Image(
                painter = image,
                modifier = imageModifier,
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "A day wandering through the sandhills " +
                        "in Shark Fin Cove, and a few of the " +
                        "sights I saw",
                style = typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "Davenport, California",
                style = typography.titleLarge
            )
            Text(
                "December 2018",
                style = typography.titleLarge
            )
            val arrayList = ArrayList<Message>()
            for (index in 0..1000) {
                arrayList.add(Message("$index"))
            }
            MessageListener(messages = arrayList)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageListener(messages: List<Message>) {
    LazyColumn {
        stickyHeader {
            Text(text = "1", Modifier.fillMaxWidth())
        }
        items(messages) { message ->
            Box(
                Modifier
                    .background(Color.Blue)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message.message,
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun DefaultPreview() {
    ComposeApplicationTheme {
//        Greeting("Android")
    }
}


data class Message(val message: String)

@Composable
fun Title(article: Article) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Text(text = article.title)
        Row {
            Text(text = "作者")
            Text(text = article.author)
        }
        Text(text = article.descMd)
    }
}
@Composable
fun Four(modifier: Modifier) {
    Text(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp), text = "Four", color = Color.White
    )
}

@Composable
fun Three(modifier: Modifier) {
    Text(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp), text = "Three", color = Color.White
    )
}

@Composable
fun Two(modifier: Modifier) {
    Text(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp), text = "Two", color = Color.White
    )
}

@Composable
fun One(modifier: Modifier) {
    Text(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 100.dp), text = "One", color = Color.White
    )
}

val blue = Color(0xFF2772F3)
val blueDark = Color(0xFF0B182E)

val Purple300 = Color(0xFFCD52FC)
val Purple700 = Color(0xFF8100EF)

@ExperimentalFoundationApi
@Composable
@Preview("Light Theme", widthDp = 360, heightDp = 640)
private fun InitSearchBox() {
    Spacer(modifier = Modifier.sizeIn(8.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyApp() {
    val viewModel: TimerViewModel = viewModel()
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "abc")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeLeftText(viewModel = viewModel)
            EditText(viewModel = viewModel)
            Row {
                StartButton(viewModel = viewModel)
                StopButton(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PasswordTextField(string: String?) {
    val articleViewModel: ArticleViewModel = viewModel()

    var password by remember {
        if (string == null) {
            mutableStateOf("")
        } else {
            mutableStateOf(string)
        }
    }

    Column {
        TextField(
            value = password,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                password = it
                articleViewModel.getArticles(0)
            },
            label = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        val openDialog = remember { mutableStateOf(false) }
        Button(onClick = { openDialog.value = true }) {
            Text("Click me")
        }
//        ShowDialog(R.string.app_name, R.string.about_content, openDialog)
    }
}

@Composable
fun ExpandingCard(data: Article, onClick: (url: String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    // describe the card for the current state of expanded
    Card(modifier = Modifier
        .background(color = Color.Gray)
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(top = 8.dp)) {
        Column(
            Modifier
                .width(280.dp)
                .animateContentSize() // automatically animate size when it changes
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .clickable {
                    onClick(data.link)
                }
        ) {
            AsyncImage(
                model = data.envelopePic,
                contentDescription = "description of the image"
            )
            Text(
                text = data.title,
                style = MaterialTheme.typography.titleSmall
            )

            // content of the card depends on the current value of expanded
            if (expanded) {
                Text(text = data.desc, Modifier.padding(top = 8.dp))
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