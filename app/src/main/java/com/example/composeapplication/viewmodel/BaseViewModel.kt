package com.example.composeapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {
    val stateLiveData = MutableLiveData<State>().apply {
        value = State.Loading
    }

    fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            kotlin.runCatching {
                block()
            }.onSuccess {
                stateLiveData.value = State.Success
            }.onFailure {
                stateLiveData.value = State.Error(it.message)
            }
        }
    }
}

sealed class State {
    object Loading : State()
    object Success : State()
    class Error(val errorMsg: String?) : State()

    fun isLoading() = this is Loading

    fun isError() = this is Error
}