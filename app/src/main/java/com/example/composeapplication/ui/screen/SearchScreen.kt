package com.example.composeapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeapplication.viewmodel.MainViewModel
import com.example.composeapplication.viewmodel.search.SearchViewModel
import com.google.accompanist.insets.statusBarsHeight


@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel()
) {
    val observeAsState by viewModel.searchResult.observeAsState()

    Column {
        Column {
            Spacer(
                modifier = Modifier
                    .statusBarsHeight()
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primaryVariant)
            )
            TopAppBar(
                title = {
                    Text(
                        text = "搜索文章",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        mainViewModel.popUp()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primaryVariant,
                elevation = 0.dp
            )
        }
        SearchContent {
            viewModel.searchArticle(it)
        }
        if (observeAsState != null) {
            ArticleList(result = observeAsState!!) { url, title ->
                mainViewModel.navigateToWebViewPage(title = title, url = url)
            }
        }
    }

}

@Composable
private fun SearchContent(search: (key: String) -> Unit) {
    var searchKey by remember {
        mutableStateOf("")
    }
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .focusRequester(focusRequester),
            value = searchKey,

            label = {
                Text(
                    text = "搜索文章",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onPrimary
                )
            },
            onValueChange = { searchValue ->
                searchKey = searchValue
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "search",
                    modifier = Modifier.clickable {
                        search(searchKey)
                    }
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    search(searchKey)
                }
            ),
            singleLine = true
        )
    }
}