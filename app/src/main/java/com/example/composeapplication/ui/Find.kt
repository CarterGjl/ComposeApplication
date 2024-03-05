package com.example.composeapplication.ui

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeapplication.R
import com.example.composeapplication.Utils
import com.example.composeapplication.bean.Movie
import com.example.composeapplication.bean.testMovies
import com.example.composeapplication.viewmodel.ArticleViewModel

@Composable
fun Find(articleViewModel: ArticleViewModel, onClick: (Movie) -> Unit) {
    val warningTip = stringResource(id = R.string.input_search_warning)
    var textFieldValue by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val baseContext = articleViewModel.getApplication<Application>().baseContext
    Column {
        Row {

            TextField(
                value = textFieldValue,

                onValueChange = {
                    textFieldValue = it
                    Utils.logDebug(Utils.TAG_SEARCH, "input:$textFieldValue")
                },

                modifier = Modifier
                    // .fillMaxWidth(0.9f)
                    .fillMaxWidth()
                    // .fillMaxHeight(0.15f)
                    .wrapContentHeight()
                    .padding(6.dp),

                textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace
                ),
                label = {
                    Text(stringResource(id = R.string.input_label_search))
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            Utils.logDebug(
                                Utils.TAG_SEARCH,
                                "search click with keyWord:$textFieldValue"
                            )

                            if (textFieldValue.length > 1) {
                                searchQuery = textFieldValue
                                if (searchQuery.isNotEmpty()) {
                                    articleViewModel.searchMoviesComposeCoroutines()
                                }
                            } else Toast.makeText(
                                baseContext,
                                warningTip,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(Icons.Outlined.Search, "search", tint = Color.White)
                    }
                },
                singleLine = true,

                shape = editShapes.large,

                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Blue,
                    focusedLabelColor = Color.Blue,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                )
            )
        }
    }
    LaunchedEffect(searchQuery) {

        Utils.logDebug(Utils.TAG_SEARCH, "searchQuery updated:$searchQuery")
        if (searchQuery.isNotEmpty()) {
            articleViewModel.searchMoviesComposeCoroutines()
        }
    }
    val movieData = remember {
        emptyList<Movie>()
    }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(2.dp)
    ) {
        items(movieData.size) { index ->
            MovieThumbnail(movieData[index], onClick = { onClick(movieData[index]) })
        }
    }
}

@Composable
fun MovieThumbnail(movie: Movie, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp),
        content = {
            Card(
                Modifier
                    .border(0.5.dp, Color.Gray, shape = MaterialTheme.shapes.small)
                    .shadow(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .clickable(onClick = onClick)
                        .wrapContentSize()
                ) {
                    val contentWidth = 100.dp
                    val contentHeight = 141.dp
                    LoadImage(
                        url = movie.Poster,
                        modifier = Modifier
                            .width(contentWidth)
                            .height(contentHeight),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = movie.Title
                    )
                    Text(
                        text = movie.Title,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .width(contentWidth)
                            .padding(3.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = nameColor,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MoviewThumbailPreview(){
    MovieThumbnail(movie = testMovies.last()) {

    }
}

@Composable
fun HelloContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        var name by remember { mutableStateOf("") }
        if (name.isNotEmpty()) {
            Text(
                text = "Hello, $name!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
    }
}