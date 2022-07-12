package com.example.composeapplication.ui.screen.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MyAppBar(
    actions: @Composable RowScope.() -> Unit = {},
    @StringRes id: Int

) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.primarySurface)
            .statusBarsPadding()
    ) {
        TopAppBar(
            actions = actions,
            title = {
                Text(
                    text = stringResource(id = id),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onPrimary,
                )
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            elevation = 0.dp
        )
    }
}
