package com.example.composeapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(
    showBackground = true, showSystemUi = true, backgroundColor = 0xFF262629,
    device = Devices.PIXEL_3, name = "MeeingItem"
)
@Composable
fun MemberItem() {
    val show = true
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(
                color = Color(0xFF262629)
            )
    ) {
        Image(
            modifier = Modifier
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(50))
                .width(19.dp),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "icon"
        )
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.wrapContentHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "金字经",
                        color = Color(0xFFE8E9EB)
                    )
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(shape = RoundedCornerShape(20))
                            .background(color = Color(0xFF8F6731))


                    ) {
                        Text(
                            modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                            text = "主持人",
                            color = Color(0xFFF7DBBA)
                        )
                    }
                }
                if (show) {
                    Text(
                        text = "等待加入",
                        color = Color(0xFF77787A),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Button(
                    modifier = Modifier
                        .height(21.dp)
                        .width(48.dp), onClick = { /*TODO*/ }, contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "呼叫")
                }
            }
            Text(text = "百度科技园K3-F2-A02", color = Color(0xFF77787A))
        }
    }
}

@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true,backgroundColor = 0xFF262629)
@Composable
fun MeetingList() {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(count = 10) {
            MemberItem()
        }
    }
}
