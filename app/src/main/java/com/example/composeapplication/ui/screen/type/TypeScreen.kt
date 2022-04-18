package com.example.composeapplication.ui.screen.type

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeapplication.R
import com.example.composeapplication.ui.screen.LoadingPage
import com.example.composeapplication.ui.screen.type.bean.TreeListResponse
import com.example.composeapplication.ui.screen.type.viewmodel.TypeTreeViewModel
import com.example.composeapplication.ui.screen.widget.MyAppBar
import com.example.composeapplication.viewmodel.State

@Composable
fun TypeScreen(
    treeViewModel: TypeTreeViewModel = viewModel(),
    onClick: (knowledge: TreeListResponse.Knowledge) -> Unit
) {
    Scaffold(
        topBar = {
            MyAppBar(id = R.string.knowledge)
        }
    ) {

        val observeAsState: List<TreeListResponse.Knowledge> by treeViewModel.knowledges.observeAsState(
            emptyList()
        )
        val state by treeViewModel.stateLiveData.observeAsState(State.Loading)

        LoadingPage(state = state, loadInit = {
            treeViewModel.getTreeResponse()
        }) {
            LazyColumn(
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(observeAsState) { knowledge ->
                    TypeListItem(knowledge = knowledge, onClick)
                }
            }
        }

    }
}


@Composable
fun TypeListItem(
    knowledge: TreeListResponse.Knowledge,
    onClick: (knowledge: TreeListResponse.Knowledge) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick(knowledge)
                }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(10.0F)) {
                Text(
                    text = knowledge.name,
                    style = MaterialTheme.typography.subtitle1
                )
                val body: String? = knowledge.children?.let { children ->
                    children.joinToString("     ", transform = { child -> child.name })
                }
                Text(
                    text = body ?: "",
                    style = MaterialTheme.typography.body2
                )
            }
            Icon(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .weight(1.0f),
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "camera"
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TypeListItemPreview() {
    TypeScreen(onClick = {

    })
}