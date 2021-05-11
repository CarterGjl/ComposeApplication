package com.example.composeapplication.ui.timer.status

import com.example.composeapplication.ui.timer.IStatus
import com.example.composeapplication.ui.timer.TimerViewModel

class NotStartedStatus(private val viewModel: TimerViewModel) : IStatus {
    override fun startButtonDisplayString(): String {
        return "Start"
    }

    override fun clickStartButton() {
        viewModel.animatorController.start()
    }

    override fun stopButtonEnabled(): Boolean {
        return false
    }

    override fun clickStopButton() {

    }

    override fun showEditText(): Boolean {
        return true
    }
}