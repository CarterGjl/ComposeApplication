package com.example.composeapplication.ui.screen.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun MyAppBar(
    actions: @Composable RowScope.() -> Unit = {},
    @StringRes id: Int

) {
    Column {
        TopAppBar(
            modifier = Modifier.statusBarsHeight(56.dp),
            actions = actions,
            title = {
                Text(
                    text = stringResource(id = id),
                    modifier = Modifier.statusBarsPadding(),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onPrimary,
                )
            },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            elevation = 0.dp
        )
    }
}
