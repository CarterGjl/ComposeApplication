package com.example.composeapplication.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class SplashViewModel : ViewModel() {

    @OptIn(DelicateCoroutinesApi::class)
    fun countDownCoroutines(
        total: Int, onTick: (Int) -> Unit, onFinish: () -> Unit,
        scope: CoroutineScope = GlobalScope
    ): Job {
        return flow {
            for (i in total downTo 0) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.Default)
            .onCompletion { onFinish.invoke() }
            .onEach { onTick.invoke(it) }
            .flowOn(Dispatchers.Main)
            .launchIn(scope)
    }

}