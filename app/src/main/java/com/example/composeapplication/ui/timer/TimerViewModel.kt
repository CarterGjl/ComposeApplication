package com.example.composeapplication.ui.timer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.composeapplication.ui.timer.status.CompletedStatus
import com.example.composeapplication.ui.timer.status.NotStartedStatus

const val MAX_INPUT_LENGTH = 5

class TimerViewModel : ViewModel() {

    var animatorController = AnimatorController(this)
    var status: MutableState<IStatus> = mutableStateOf(NotStartedStatus(this))

    var totalTime = mutableStateOf(0L)
    var timeLef = mutableStateOf(0L)
    fun updateValue(text: String) {
        if (status.value is CompletedStatus) {
            status.value = NotStartedStatus(this)
        }
        if (text.length > MAX_INPUT_LENGTH) {
            return
        }
        var value = text.replace("/D".toRegex(), "")
        if (value.startsWith("0")) {
            value = value.substring(1)
        }
        if (value.isBlank()) {
            value = "0"
        }
        totalTime.value = value.toLong()
        timeLef.value = value.toLong()
    }
}