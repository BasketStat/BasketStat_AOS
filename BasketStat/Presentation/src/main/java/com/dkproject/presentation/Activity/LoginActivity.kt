package com.dkproject.presentation.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dkproject.presentation.ui.screen.login.GoogleAuthUiClient
import com.dkproject.presentation.ui.screen.login.LoginNavigation
import com.dkproject.presentation.ui.screen.login.LoginScreen
import com.dkproject.presentation.ui.screen.login.LoginViewModel
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val viewModel = viewModel<LoginViewModel>()
            val state = viewModel.state.collectAsState().value

            LaunchedEffect(state.navigation) {
                when(state.navigation) {
                    LoginNavigation.SignUp -> {
                        context.startActivity(Intent(context, SignUpActivity::class.java))
                        viewModel.navigationReset()
                    }
                    LoginNavigation.Home -> {
                        context.startActivity(Intent(context, HomeActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                    }
                    LoginNavigation.Default -> {}
                }
            }

            val googleAuthUiClient by lazy {
                GoogleAuthUiClient(
                    context = context,
                    oneTapClient = Identity.getSignInClient(context)
                )
            }
            val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if(result.resultCode == RESULT_OK) {
                    lifecycleScope.launch {
                        val signInResult = googleAuthUiClient.signInWithIntent(intent = result.data ?: return@launch)
                        viewModel.googleLogin()
                    }
                }
            }
            LoginScreen(viewModel = viewModel) {
                lifecycleScope.launch {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            intentSender = signInIntentSender ?: return@launch
                        ).build()
                    )
                }

            }
        }
    }
}