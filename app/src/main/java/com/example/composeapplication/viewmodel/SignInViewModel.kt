package com.example.composeapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapplication.model.RemoteSevice
import kotlinx.coroutines.launch

private const val TAG = "SignInViewModel"

class SignInViewModel : ViewModel() {

    private val _state = MutableLiveData<SignInState>()

    val state: LiveData<SignInState> = _state

    fun signIn(email: String, pwd: String) {
        viewModelScope.launch {
            val login = RemoteSevice.getInstance().login(email, pwd)
            if (login.errorCode != 0) {
                _state.value = SignInState.Error(login.errorMsg)
            } else {
                _state.value = SignInState.SignedIn
            }
            Log.d(TAG, "signIn: $login")
        }
    }
}

sealed class SignInState {
    object SignedOut : SignInState()
    object InProgress : SignInState()
    data class Error(val error: String) : SignInState()
    object SignedIn : SignInState()
}