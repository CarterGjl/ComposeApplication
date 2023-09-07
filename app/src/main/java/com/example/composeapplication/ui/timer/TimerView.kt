package com.example.composeapplication.ui.timer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TimeLeftText(viewModel: TimerViewModel) {
    Text(
        text = TimeFormatUtils.formatTime(viewModel.timeLef.longValue),
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun EditText(viewModel: TimerViewModel) {
    Box(modifier = Modifier.size(300.dp, 120.dp), contentAlignment = Alignment.Center) {
        if (viewModel.status.value.showEditText()) {
            TextField(
                modifier = Modifier.size(200.dp, 60.dp),
                value = if (viewModel.totalTime.longValue == 0L) "" else viewModel.totalTime.longValue.toString(),
                onValueChange = viewModel::updateValue,
                label = { Text(text = "Countdown Seconds") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun StartButton(viewModel: TimerViewModel) {
    Button(modifier = Modifier
        .width(150.dp)
        .padding(16.dp),
        enabled = viewModel.totalTime.longValue > 0,
        onClick = {
            viewModel.status.value.clickStartButton()
        })
    {
        Text(text = viewModel.status.value.startButtonDisplayString())
    }
}

@Composable
fun StopButton(viewModel: TimerViewModel) {
    Button(
        modifier = Modifier
            .width(150.dp)
            .padding(16.dp),
        enabled = viewModel.status.value.stopButtonEnabled(),
        onClick = { viewModel.status.value.stopButtonEnabled() })
    {
        Text(text = viewModel.status.value.startButtonDisplayString())
    }
}