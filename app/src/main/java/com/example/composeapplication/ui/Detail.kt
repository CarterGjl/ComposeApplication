package com.example.composeapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapplication.R
import com.example.composeapplication.bean.MoviePro
import com.example.composeapplication.bean.testMoviePro

@Preview(showBackground = true)
@Composable
fun DetailPreview(){
    Detail(moviePro = testMoviePro)
}
@Composable
fun Detail(moviePro: MoviePro) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.TopEnd) {
                LoadImage(
                    url = moviePro.Poster, contentDescription = moviePro.Title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    contentScale = ContentScale.FillBounds,
                )
            }
            val checkedState = remember { mutableStateOf(false) }
            Card(
                modifier = Modifier.padding(6.dp),
                shape = RoundedCornerShape(50),
                // elevation = 8.dp,
                backgroundColor = likeColorBg
            ) {
                IconToggleButton(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(32.dp),
                    checked = checkedState.value,
                    onCheckedChange = {
                        checkedState.value = it
                    }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_like),
                        contentDescription = "like",
                        tint = if (checkedState.value) Color.Blue else Color.White
                    )
                }
            }
        }
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(0.9f)
                        .align(Alignment.CenterVertically),
                    text = moviePro.Title,
                    style = MaterialTheme.typography.h6,
                    color = nameColor,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = "Ratings:",
                    style = MaterialTheme.typography.caption,
                    color = nameColor,
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = moviePro.imdbRating,
                    style = MaterialTheme.typography.h6,
                    color = nameColor,
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = moviePro.Director,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = moviePro.Actors,
                style = MaterialTheme.typography.subtitle2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = moviePro.Plot,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .width(250.dp)
                        .weight(0.5f),
                    onClick = {},
                    shape = shapes.large,
                    colors = ButtonDefaults.buttonColors(backgroundColor = infoButtonColor)
                ) {
                    Text(
                        text = stringResource(id = R.string.movie_info_rent),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    modifier = Modifier
                        .width(250.dp)
                        .weight(0.5f),
                    onClick = {},
                    shape = shapes.large,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
                ) {
                    Text(
                        text = stringResource(id = R.string.movie_info_buy),
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = moviePro.Type + "・",
                    style = MaterialTheme.typography.caption,
                )

                Text(
                    text = moviePro.Year + "・",
                    style = MaterialTheme.typography.caption,
                )

                Text(
                    text = moviePro.Runtime,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}