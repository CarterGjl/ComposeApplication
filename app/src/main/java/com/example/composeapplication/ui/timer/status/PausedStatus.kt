package com.example.composeapplication.ui.timer.status

import com.example.composeapplication.ui.timer.IStatus
import com.example.composeapplication.ui.timer.TimerViewModel

class PausedStatus(private val viewModel: TimerViewModel):IStatus {
    override fun startButtonDisplayString(): String {
        return "Resume"
    }

    override fun clickStartButton() {
        viewModel.animatorController.resume()
    }

    override fun stopButtonEnabled(): Boolean {
        return true
    }

    override fun clickStopButton() {
        viewModel.animatorController.stop()
    }

    override fun showEditText(): Boolean {
        return false
    }
}