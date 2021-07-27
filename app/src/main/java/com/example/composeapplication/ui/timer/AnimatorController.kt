package com.example.composeapplication.ui.timer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import com.example.composeapplication.ui.timer.status.CompletedStatus
import com.example.composeapplication.ui.timer.status.PausedStatus
import com.example.composeapplication.ui.timer.status.StartedStatus

@Suppress("unused")
class AnimatorController(private val viewModel: TimerViewModel) {

    private var valueAnimator: ValueAnimator? = null

    fun start() {
        if (viewModel.totalTime.value == 0L) return
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(viewModel.totalTime.value.toInt(), 0)
            valueAnimator?.interpolator = LinearInterpolator()
            valueAnimator?.addUpdateListener {
                viewModel.timeLef.value = (it.animatedValue as Int).toLong()
            }
            valueAnimator?.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    complete()
                }
            })
        } else {
            valueAnimator?.setIntValues(viewModel.totalTime.value.toInt(), 0)
        }
        valueAnimator?.duration = viewModel.totalTime.value * 1000L
        valueAnimator?.start()
        viewModel.status.value = StartedStatus(viewModel)
    }

    private fun complete() {
        viewModel.totalTime.value = 0
    }

    fun pause() {
        valueAnimator?.pause()
        viewModel.status.value = PausedStatus(viewModel)
    }

    fun resume() {
        valueAnimator?.resume()
        viewModel.status.value = StartedStatus(viewModel)
    }

    fun stop() {
        valueAnimator?.cancel()
        viewModel.timeLef.value = 0
        viewModel.status.value = CompletedStatus(viewModel)
    }

}