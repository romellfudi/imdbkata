package com.example.login.ui.views

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.repeatOnLifecycle
import com.example.core.view.*
import com.example.login.R
import com.example.login.helpers.LoginViewModelState
import com.example.login.ui.viewmodels.RegisterViewModel


/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    backScreen: () -> Unit,
    nextScreen: () -> Unit,
    displayMessage: (String) -> Unit,
    isDark: Boolean = isSystemInDarkTheme()
) {

    val nameText by viewModel.name.collectAsState()
    val mailText by viewModel.email.collectAsState()
    val passwordText by viewModel.password.collectAsState()
    val isMailValid by viewModel.isEmailValid.collectAsState(false)
    val isPasswordValid by viewModel.isPasswordValid.collectAsState(false)
    val isReadyToLogin by viewModel.isReadyToRegister.collectAsState(false)
    var passwordVisible by remember { mutableStateOf(false) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val welcomeFormat = stringResource(R.string.welcome_format)

    LaunchedEffect("back observer") {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.shouldBack.collect { shouldBack ->
                if (shouldBack) {
                    backScreen()
                }
            }
        }
    }
    LaunchedEffect("register observer") {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.shouldRegister.collect { it ->
                when (it) {
                    is LoginViewModelState.Success -> {
                        nextScreen()
                        displayMessage(welcomeFormat.format(it.userEmailOrName))
                    }
                    is LoginViewModelState.Error -> {
                        displayMessage(it.message)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.cleanView()
    }

    val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val callback = remember(backScreen) {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backScreen()
            }
        }
    }

    DisposableEffect(key1 = backPressDispatcher) {
        callback.isEnabled = true
        backPressDispatcher?.addCallback(callback)
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                callback.remove()
            }
        }
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            callback.remove()
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color5)
    )
    Column(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        TopAppBar(title = { Text("") },
            backgroundColor = Color5,
            contentColor = Color2,
            navigationIcon = {
                IconButton(onClick = { viewModel.onBack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            })

        Image(
            painter = painterResource(id = R.drawable.imdb_rectangular),
            contentDescription = "IMDB logo",
            modifier = Modifier.padding(20.dp)
        )

        Text(
            modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
            text = stringResource(R.string.user_creating_text),
            fontSize = 30.sp,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                color = if (!isDark) Color2 else Color4)
        )

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(modifier = Modifier
            .semantics { testTag = "register_screen_name" }
            .padding(20.dp, 0.dp, 20.dp, 0.dp),
            colors = getTextFieldColors(isDark),
            label = { Text(text = stringResource(R.string.name)) },
            value = nameText,
            onValueChange = { viewModel.setName(it) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            modifier = Modifier
                .semantics { testTag = "register_screen_email" }
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            colors = getTextFieldColors(isDark),
            label = { Text(text = stringResource(R.string.mail)) },
            value = mailText,
            onValueChange = { viewModel.email.value = it.lowercase().trim() },
            isError = !isMailValid,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(modifier = Modifier
            .semantics { testTag = "register_screen_password" }
            .padding(20.dp, 0.dp, 20.dp, 0.dp),
            colors = getTextFieldColors(isDark),
            label = { Text(text = stringResource(R.string.password)) },
            value = passwordText,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = !isPasswordValid,
            onValueChange = { viewModel.password.value = it.trim() },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = "Visibility"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(5.dp))
        Box(modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)) {
            Text(
                text = AnnotatedString(stringResource(R.string.user_register_tips)),
                style = TextStyle(
                    color = Color3,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Left
                )
            )
        }

        RegisterButton(
            modifier = Modifier
                .semantics { testTag = "register_screen_register_button" }
                .fillMaxWidth()
                .height(50.dp),
            registerAction = { viewModel.register(nameText.capitalize(), mailText, passwordText) },
            isReadyToLogin = isReadyToLogin
        )
    }
}

@Composable
fun RegisterButton(
    modifier: Modifier = Modifier,
    registerAction: () -> Unit,
    isReadyToLogin: Boolean
) {
    Spacer(modifier = Modifier.height(40.dp))
    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
        Button(
            onClick = { registerAction() },
            shape = RoundedCornerShape(15.dp),
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isReadyToLogin) Color4 else Color3,
                contentColor = Color5
            ),
            enabled = isReadyToLogin
        ) {
            Text(
                text = stringResource(R.string.accept), style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}