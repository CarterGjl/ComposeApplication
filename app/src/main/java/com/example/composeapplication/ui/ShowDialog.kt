package com.example.composeapplication.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.composeapplication.R

@Composable
fun ShowDialog(
    title: Int,
    message: Int,
    openDialog: MutableState<Boolean>
) {
    if (openDialog.value)
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(
                    text = stringResource(id = title),
                    color = nameColor
                )
            },
            text = {
                Text(
                    text = stringResource(id = message),
                    color = nameColor
                )
            },
            confirmButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(
                        text = stringResource(id = R.string.search_dialog_ok),
                        color = Color.White
                    )
                }
            },
            shape = shapes.large,
        )
}
