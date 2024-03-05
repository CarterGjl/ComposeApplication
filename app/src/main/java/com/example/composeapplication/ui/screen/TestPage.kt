package com.example.composeapplication.ui.screen

import OffsetOverscrollEffect
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.example.composeapplication.ui.base.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TestPage(

) {

    val scope = rememberCoroutineScope()
    val overscroll = remember(scope) { OffsetOverscrollEffect(scope) }
    LazyColumn(modifier = Modifier.overscroll(overscroll)) {
        for (i in 1..20){
            item {
                CardItem()
            }
        }

    }
//    val offset = remember { mutableStateOf(0f) }
//    val scope = rememberCoroutineScope()
//    // Create the overscroll controller
//    val overscroll = remember(scope) { OffsetOverscrollEffect(scope) }
//    // let's build a scrollable that scroll until -512 to 512
//    val scrollStateRange = (-512f).rangeTo(512f)
//    Box(
//        Modifier
//            .size(150.dp)
//            .scrollable(
//                orientation = Orientation.Vertical,
//                state = rememberScrollableState { delta ->
//                    // use the scroll data and indicate how much this element consumed.
//                    val oldValue = offset.value
//                    // coerce to our range
//                    offset.value = (offset.value + delta).coerceIn(scrollStateRange)
//
//                    offset.value - oldValue // indicate that we consumed what's needed
//                },
//                // pass the overscroll to the scrollable so the data is updated
//                overscrollEffect = overscroll
//            )
//            .background(Color.LightGray),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            offset.value.roundToInt().toString(),
//            style = TextStyle(fontSize = 32.sp),
//            modifier = Modifier
//                // show the overscroll only on the text, not the containers (just for fun)
//                .overscroll(overscroll)
//        )
//    }

//    var pageStateData by remember {
//        mutableStateOf(PageState.CONTENT.bindData())
//    }
//    var isCustomLayout by remember {
//        mutableStateOf(false)
//    }
//    Box(modifier = Modifier.fillMaxSize()) {
//        DefaultStateLayout(
//            modifier = Modifier.fillMaxSize(),
//            pageStateData = pageStateData,
//            onRetry = {
//                pageStateData = PageState.LOADING.bindData()
//            },
//            loading = {
//                if (isCustomLayout) {
//                    CustomLoadingLayout(it)
//                } else {
//                    DefaultLoadingLayout(it)
//                }
//            }
//        ) {
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(text = "加载成功内容", style = MaterialTheme.typography.h5)
//            }
//        }
//
//        Column(
//            modifier = Modifier
//                .padding(0.dp, 20.dp)
//                .fillMaxWidth()
//                .wrapContentHeight()
//                .align(Alignment.BottomCenter)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                BottomButton(text = "加载中") {
//                    pageStateData = PageState.LOADING.bindData()
//                }
//                BottomButton(text = "空页面") {
//                    pageStateData = PageState.EMPTY.bindData()
//                }
//            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                BottomButton(text = "加载失败") {
//                    pageStateData = PageState.ERROR.bindData()
//                }
//                BottomButton(text = "加载成功") {
//                    pageStateData = PageState.CONTENT.bindData()
//                }
//            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                BottomButton(text = "自定义加载中文案") {
//                    pageStateData = PageState.LOADING.bindData(StateData(tipTex = "自定义加载中文案"))
//                }
//                BottomButton(text = "自定义加载中布局") {
//                    isCustomLayout = !isCustomLayout
//                }
//            }
//        }
//    }
}

@Composable
fun BottomButton(text: String, callback: () -> Unit) {
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .width(150.dp)
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Blue)
            .padding(0.dp, 10.dp)
            .clickable {
                callback.invoke()
            })
}

@Composable
fun CustomLoadingLayout(stateLayoutData: StateLayoutData) {
    stateLayoutData.pageStateData.let {
        (it.tag as? StateData)?.let { item ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = item.tipTex ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    CircularProgressIndicator()
                    Text(
                        text = "自定义布局",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}


@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun TestPagePreview() {
    TestPage()
}

@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun CardViewPreview() {
    CardItem()

}

@Composable
private fun CardItem() {
    Column {

        var checked by remember { mutableStateOf(true) }
        Switch(modifier = Modifier.fillMaxSize(), checked = checked, onCheckedChange = {
            checked = !checked
        })
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Gray)
                .wrapContentHeight()
                .padding(top = 8.dp),
            elevation = CardDefaults.cardElevation()
        ) {
            Text(
                text = "This is a Card",
                modifier = Modifier.height(90.dp)
            )
        }
    }
}


//                    val insets = LocalWindowInsets.current
//                    // 切记，这些信息都是px单位，使用时要根据需求转换单位
//                    val imeBottom = with(LocalDensity.current) { insets.navigationBars.bottom.toDp() }
//                    Scaffold(bottomBar = {
//                        BottomNavigation(Modifier.height(height = 56.dp + imeBottom)) {
//                            repeat(5) {
//                                BottomNavigationItem(
//                                    modifier = Modifier.navigationBarsPadding(),
//                                    icon = {
//                                        Icon(Icons.Filled.AccessTime, contentDescription = "")
//
//                                    },
//
//                                    label = {
//                                        Text(
//                                            text = "$it",
//                                        )
//                                    }, onClick = {
//
//                                    }, selected = true
//                                )
//                            }
//
//
//                        }
//                    }) {
//                    }

@OptIn(ExperimentalFoundationApi::class)
class NoAOpOverscrollEffect : OverscrollEffect {
    override fun applyToScroll(
        delta: Offset,
        source: NestedScrollSource,
        performScroll: (Offset) -> Offset
    ): Offset = performScroll(delta)

    override suspend fun applyToFling(
        velocity: Velocity,
        performFling: suspend (Velocity) -> Velocity
    ) { performFling(velocity) }

    override val isInProgress: Boolean
        get() = false

    override val effectModifier: Modifier
        get() = Modifier
}
