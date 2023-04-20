@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.login.ui.views

/**
 * @author @romellfudi
 * @date 2023-03-16
 * @version 1.0.a
 */
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.core.view.*
import com.example.core.view.compose.AppResourceImage
import com.example.core.view.compose.RoundedSocialButton
import com.example.login.R
import com.example.login.helpers.LoginViewModelState
import com.example.login.ui.viewmodels.LoginViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    registerScreen: () -> Unit,
    homeScreen: () -> Unit,
    displayMessage: (String) -> Unit,
    isDark: Boolean = isSystemInDarkTheme()
) {

    val usernameText by viewModel.email.collectAsState()
    val passwordText by viewModel.password.collectAsState()
    val isReadyToLogin by viewModel.isReadyToLogin.collectAsState(false)
    val welcomeFormat = stringResource(R.string.welcome_format)
    val guestName = stringResource(R.string.guest)
    var passwordVisible by remember { mutableStateOf(false) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect("register registerUserObserver") {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.shouldNavigateRegister.collect { registerScreen() }
        }
    }

    LaunchedEffect("register goHomeObserver") {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.shouldNavigateHome.collect {
                when (it) {
                    is LoginViewModelState.Success -> {
                        val name = it.userEmailOrName.ifEmpty { guestName }
                        displayMessage(welcomeFormat.format(name))
                        homeScreen()
                    }
                    is LoginViewModelState.Error -> {
                        displayMessage(welcomeFormat.format(it.message))
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPrimary)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppResourceImage(
            R.drawable.imdb,
            modifier = Modifier
                .size(250.dp)
        )

        OutlinedTextField(
            modifier = Modifier.semantics { testTag = "login_screen_email" },
            colors = getTextFieldColors(isDark),
            label = { Text(text = stringResource(R.string.mail)) },
            value = usernameText,
            onValueChange = { viewModel.setUsernameText(it) },
        )

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            modifier = Modifier.semantics { testTag = "login_screen_password" },
            colors = getTextFieldColors(isDark),
            label = { Text(text = stringResource(R.string.password)) },
            value = passwordText,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { viewModel.setUPasswordText(it) },
            singleLine = true,
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
        Box(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
            ClickableText(
                text = AnnotatedString(stringResource(R.string.forgot_password)),
                onClick = { },
                style = TextStyle(
                    color = ColorDarkSecondary,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Left
                ),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = -50.dp),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = { viewModel.loginWithEmailPassword() },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .semantics { testTag = "login_screen_login_button" }
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isReadyToLogin) colorDarkTertiary else ColorDarkSecondary,
                    contentColor = ColorSecondary
                ),
                enabled = isReadyToLogin
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(R.string.try_text))

        Spacer(modifier = Modifier.height(10.dp))
        Row {
            AppleButton(onAuthComplete = {
//                viewModel.signApple(it),
            }, onAuthError = {})
            Spacer(modifier = Modifier.width(20.dp))
            FacebookButton(onAuthComplete = viewModel::signFacebook,
                onAuthError = {})
            Spacer(modifier = Modifier.width(20.dp))
            GoogleButton(onAuthComplete = viewModel::signGoogle,
                onAuthError = {})
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Text(text = stringResource(R.string.not_has_account))
            Spacer(modifier = Modifier.width(5.dp))
            ClickableText(
                modifier = Modifier.semantics { testTag = "login_screen_new_register_button" },
                text = AnnotatedString(stringResource(R.string.to_register)),
                onClick = { viewModel.goToRegisterView() },
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 30.dp, 0.dp)) {
            ClickableText(
                modifier = Modifier.semantics { testTag = "login_screen_guest_action" },
                text = AnnotatedString(stringResource(R.string.guest_text)),
                onClick = { viewModel.signAnonymous() },
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }

    }
}

@Composable
fun AppleButton(
    onAuthComplete: () -> Unit,
    onAuthError: (Exception) -> Unit,
) {
    RoundedSocialButton(
        icon = painterResource(id = R.drawable.apple),
        onClick = { },
        contentDescription = stringResource(R.string.apple)
    )
}

@Composable
fun FacebookButton(
    onAuthComplete: (AccessToken) -> Unit,
    onAuthError: (Exception) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {
        // nothing to do. handled in FacebookCallback
    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                // do nothing
            }

            override fun onError(error: FacebookException) {
                onAuthError(error)
            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    onAuthComplete(result.accessToken)
                }
            }
        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }
    RoundedSocialButton(
        icon = painterResource(id = R.drawable.facebook),
        onClick = { launcher.launch(listOf("email", "public_profile")) },
        contentDescription = stringResource(R.string.facebook)
    )
}

@Composable
fun GoogleButton(
    onAuthComplete: (AuthCredential) -> Unit,
    onAuthError: (Exception) -> Unit,
) {
    val context = LocalContext.current
    val clientId = stringResource(R.string.default_web_client_id)
    val gsoBuilder = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(clientId)
        .requestEmail()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                onAuthComplete(credential)
            } catch (e: ApiException) {
                onAuthError(e)
            }
        }

    RoundedSocialButton(
        icon = painterResource(id = R.drawable.google),
        onClick = {
            val client = GoogleSignIn.getClient(context, gsoBuilder.build())
            launcher.launch(client.signInIntent)
        },
        contentDescription = stringResource(R.string.google)
    )
}