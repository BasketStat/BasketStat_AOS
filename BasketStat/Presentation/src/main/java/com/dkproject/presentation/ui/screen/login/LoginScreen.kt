package com.dkproject.presentation.ui.screen.login

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.components.LoginButton
import com.dkproject.presentation.ui.theme.BasketStatTheme
import com.dkproject.presentation.ui.theme.background
import com.google.android.gms.auth.api.identity.Identity


@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    message: String?,
    googleLoginLauncher: () -> Unit = {},
) {
    val loading by viewModel.loading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() } //스낵바 상태

    LaunchedEffect(key1 = message) {
        message?.let {
            snackbarHostState.showSnackbar(message = message)
            viewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = background)
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo), contentDescription = "",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f),
                )
                Spacer(modifier = Modifier.height(148.dp))
                LoginButton(
                    modifier=Modifier.fillMaxWidth(0.8f),
                    logoImage = ImageVector.vectorResource(id = R.drawable.kakaologin),
                    backgroundColor = Color(0xFFFFE800),
                    text = stringResource(id = R.string.KakaoLogin),
                    textColor = Color.Black,
                    logoWidth = 24.dp,
                    logoHeight = 22.dp
                ) {
                    //카카오 로그인
                    viewModel.kakaoLogin()
                }
                Spacer(modifier = Modifier.height(14.dp))
                LoginButton(
                    modifier=Modifier.fillMaxWidth(0.8f),
                    logoImage = ImageVector.vectorResource(id = R.drawable.applelogin),
                    backgroundColor = Color(0xFF4C4C4C),
                    text = stringResource(id = R.string.appleLogin),
                    textColor = Color.White,
                    logoWidth = 16.dp,
                    logoHeight = 18.dp
                ) {
                    //애플로그인
                    viewModel.appleLogin()
                }
                Spacer(modifier = Modifier.height(14.dp))
                LoginButton(
                    modifier=Modifier.fillMaxWidth(0.8f),
                    logoImage = ImageVector.vectorResource(id = R.drawable.googlelogin),
                    backgroundColor = Color.White,
                    text = stringResource(id = R.string.googleLogin),
                    textColor = Color.Black,
                    logoWidth = 20.dp,
                    logoHeight = 20.dp
                ) {
                    //구글 로그인
                    googleLoginLauncher()
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    BasketStatTheme {
        //LoginScreen()
    }
}