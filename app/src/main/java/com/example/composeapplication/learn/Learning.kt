package com.example.composeapplication.learn

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeapplication.bean.Article
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.Navigation
import com.example.composeapplication.ui.timer.*

//@Preview(showBackground = true)
//@Composable
//fun NewsStory() {
//    val image = painterResource(R.drawable.header)
//    MaterialTheme {
//        val typography = MaterialTheme.typography
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            val imageModifier = Modifier
//                .height(180.dp)
//                .fillMaxWidth()
//                .clickable {
//                    Log.d(TAG, "NewsStory: ")
//                }
//                .clip(shape = RoundedCornerShape(4.dp))
//            Image(
//                painter = image,
//                modifier = imageModifier,
//                contentScale = ContentScale.Crop,
//                contentDescription = ""
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                "A day wandering through the sandhills " +
//                        "in Shark Fin Cove, and a few of the " +
//                        "sights I saw",
//                style = typography.h6,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis
//            )
//            Text(
//                "Davenport, California",
//                style = typography.body2
//            )
//            Text(
//                "December 2018",
//                style = typography.body2
//            )
//            val arrayList = ArrayList<Message>()
//            for (index in 0..1000) {
//                arrayList.add(Message("$index"))
//            }
//            MessageListener(messages = arrayList)
//        }
//    }
//}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageListener(messages: List<Message>) {
    LazyColumn() {
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

//enum class CourseTabs(
//    @StringRes val title: Int,
//    @DrawableRes val icon: Int
//) {
//    HOME_PAGE(R.string.home_page, R.drawable.ic_launcher_background),
//    PROJECT(R.string.project, R.drawable.ic_launcher_background),
//    OFFICIAL_ACCOUNT(R.string.official_account, R.drawable.ic_launcher_background),
//    MINE(R.string.mine, R.drawable.ic_launcher_background)
//}


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
//@Composable
//fun Home() {
//    val (selectedTab, setSelectedTab) = remember {
//        mutableStateOf(CourseTabs.HOME_PAGE)
//    }
//    val tabs = CourseTabs.values()
//    Scaffold(backgroundColor = MaterialTheme.colors.primarySurface, bottomBar = {
//        BottomNavigation(Modifier.height(56.dp)) {
//            tabs.forEach { tab ->
//                BottomNavigationItem(
//                    icon = { Icon(painterResource(tab.icon), contentDescription = null) },
//                    label = { Text(stringResource(tab.title).toUpperCase(Locale.ROOT)) },
//                    selected = tab == selectedTab,
//                    onClick = { setSelectedTab(tab) },
//                    alwaysShowLabel = false,
//                    selectedContentColor = MaterialTheme.colors.secondary,
//                    unselectedContentColor = LocalContentColor.current,
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//        }
//    }) { innerPadding ->
//        val padding = Modifier.padding(innerPadding)
//        when (selectedTab) {
//            CourseTabs.HOME_PAGE -> {
//                One(padding)
//            }
//            CourseTabs.MINE -> {
//                Two(padding)
//            }
//            CourseTabs.OFFICIAL_ACCOUNT -> {
//                Three(padding)
//            }
//            CourseTabs.PROJECT -> {
//                Four(padding)
//            }
//            else -> {
//
//            }
//        }
//    }
//}

//@Composable
//fun Greeting(name: String = "test") {
//    val painterResource = painterResource(id = R.drawable.header)
//    Column {
//        Image(painter = painterResource, contentDescription = "for test")
//
//        Text(name)
//        Button(
//            onClick = {
//                Log.d(TAG, "Greeting: ")
//            },
//        ) {
//            Text(text = name)
//        }
//    }
//}

@ExperimentalFoundationApi
@Composable
@Preview("Light Theme", widthDp = 360, heightDp = 640)
private fun InitSearchBox() {
    Spacer(modifier = Modifier.sizeIn(8.dp))
    Navigation()
}

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
            modifier = Modifier.fillMaxSize(),
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