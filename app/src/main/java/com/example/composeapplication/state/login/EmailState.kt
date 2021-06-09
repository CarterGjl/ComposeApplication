package com.example.composeapplication.state.login

import java.util.regex.Pattern

class EmailState : TextFieldState(validator = ::isEmailValid, errorFor = ::emailValidationError)

private fun emailValidationError(email: String): String {
    return "Invalid email: $email"
}

// Consider an email valid if there's some text before and after a "@"
private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+)\$"

private fun isEmailValid(email: String): Boolean {
    return true
}