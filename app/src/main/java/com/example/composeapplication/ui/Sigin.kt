package com.example.composeapplication.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.composeapplication.R
import com.example.composeapplication.state.login.EmailState
import com.example.composeapplication.state.login.PasswordState
import com.example.composeapplication.state.login.TextFieldState
import com.example.composeapplication.viewmodel.SignInState
import com.example.composeapplication.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

//@Composable
//fun SignInScreen(viewModel: SignInViewModel = viewModel()) {
//    val viewState by viewModel.state.observeAsState(initial = SignInState.SignedOut)
//    Surface(Modifier.fillMaxSize()) {
//        SignInContent(
//            state = viewState,
//            onSignIn = viewModel::signIn,
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}

@Composable
fun SignInContent(
    state: SignInState,
    onSignIn: (email: String, password: String) -> Unit,
    modifier: Modifier
) {
    when (state) {
//        SignInState.SignedOut -> SignIn(onSignIn)
        SignInState.InProgress -> SignInProgress()
        is SignInState.Error -> SignInError(state.error)
        SignInState.SignedIn -> Text(text = "success")
        else -> {}
    }
}

@Composable
fun SignInError(error: String) {
    Text(text = error)
}

@Composable
fun SignInProgress() {
    CircularProgressIndicator()
}

//@Composable
//fun SignIn(onSignIn: (email: String, password: String) -> Unit) {
//    var email by remember {
//        mutableStateOf("")
//    }
//    var pwd by remember {
//        mutableStateOf("")
//    }
//    Column {
//        TextField(value = email, onValueChange = {
//            email = it
//        }, label = {
//            Text(text = "email")
//        }, singleLine = true)
//        TextField(value = pwd, onValueChange = {
//            pwd = it
//        }, label = {
//            Text(text = "pwd")
//        }, singleLine = true,
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Button(onClick = {
//            onSignIn.invoke(email, pwd)
//        }) {
//            Text(text = "login")
//        }
//
//        Fool().also {
//            Log.d(TAG, "SignIn: Fool")
//        }
//    }
//}
private const val TAG = "Sigin"

@Composable
fun Fool() {
    var text by remember {
        mutableStateOf(0)
    }
    Log.d(TAG, "Fool: ")
    Button(onClick = {
        text += 1
    }.also {
        Log.d(TAG, "Button")
    }) {
        SideEffect {
            Log.d(TAG, "SideEffect")
        }
        LaunchedEffect(key1 = text){
            Log.d(TAG, "LaunchedEffect")
        }
        Log.d(TAG, "Button content lambda")
        Text("$text").also { Log.d(TAG, "Text") }
    }

}

@Composable
fun Email(
    emailState: TextFieldState = remember { EmailState() },
    imeAction: ImeAction = ImeAction.Next,
    omImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = emailState.text,
        onValueChange = {
            emailState.text = it
        },
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.body2
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                emailState.onFocusChange(focused = focusState.isFocused)
                if (!focusState.isFocused) {
                    emailState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        isError = emailState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                omImeAction()
            }
        )
    )

    emailState.getError()?.let{ error ->
        TextFieldError(textError = error)
    }
}

@Composable
fun Password(
    label: String,
    passwordState: TextFieldState,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    val showPassword = remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        value = passwordState.text,
        onValueChange = {
            passwordState.text = it
            passwordState.enableShowErrors()
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                passwordState.onFocusChange(focused = focusState.isFocused)
                if (!focusState.isFocused) {
                    passwordState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2
                )
            }
        },
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.hide_password)
                    )
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(id = R.string.show_password)
                    )
                }
            }
        },
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        isError = passwordState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        )
    )
    passwordState.getError()?.let { error -> TextFieldError(textError = error) }
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error)
        )
    }
}

@Composable
fun SignInScreen(viewModel: SignInViewModel) {
    SignIn(
        onNavigationEvent = { event ->
            when (event) {
                is SignInEvent.SignIn -> {
                    viewModel.signIn(event.email,event.password)
                }
                SignInEvent.NavigateBack -> {

                }
                SignInEvent.SignInAsGuest -> {

                }
                SignInEvent.SignUp -> {

                }
            }
        }
    )
}

@Composable
fun SignIn(onNavigationEvent: (SignInEvent) -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxWidth()) {
        SignInContent(
            onSignInSubmitted = { email, password ->
                onNavigationEvent(SignInEvent.SignIn(email, password))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "snackbarErrorText",
                        actionLabel = "snackbarActionLabel"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.forgot_password))
        }
    }
}

@Composable
fun SignInContent(
    onSignInSubmitted: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        val emailState = remember { EmailState() }
        Email(emailState, omImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        val passwordState = remember { PasswordState() }
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { onSignInSubmitted(emailState.text, passwordState.text) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSignInSubmitted(emailState.text, passwordState.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid && passwordState.isValid
        ) {
            Text(
                text = stringResource(id = R.string.sign_in)
            )
        }
    }
}

@Composable
fun MineScreen(viewModel: SignInViewModel = viewModel(),navController: NavController) {
    val viewState:SignInState by viewModel.state.observeAsState(initial = SignInState.SignedOut)
    when (viewState) {
        is SignInState.SignedOut -> SignInScreen(viewModel = viewModel)
        SignInState.InProgress -> SignInProgress()
        SignInState.SignedIn -> {
            navController.navigate("mine"){
                popUpTo("article")
            }
        }
        is SignInState.Error -> {
            SignInError((viewState as SignInState.Error).error)
        }
    }
}

sealed class SignInEvent {
    data class SignIn(val email: String, val password: String) : SignInEvent()
    object SignUp : SignInEvent()
    object SignInAsGuest : SignInEvent()
    object NavigateBack : SignInEvent()
}