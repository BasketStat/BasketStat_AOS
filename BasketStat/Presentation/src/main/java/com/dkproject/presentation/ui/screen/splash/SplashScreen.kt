package com.dkproject.presentation.ui.screen.splash

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dkproject.presentation.Activity.HomeActivity
import com.dkproject.presentation.Activity.LoginActivity
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.theme.splashColor

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val snackbarMessage by viewModel.snackbarMessages.collectAsState() //스낵바 이벤트 상태
    val loading by viewModel.loading.collectAsState() //로딩상태
    val navigation by viewModel.navigateEvent.collectAsState()  //네비게이션
    val snackbarHostState = remember { SnackbarHostState() } //스낵바 상태

    LaunchedEffect(key1 = snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message)
            viewModel.clearSnackbarMessage()
        }
    }

    LaunchedEffect(key1 = navigation) {
        when (navigation) {
            SplashNavigation.Default -> {}
            SplashNavigation.Login -> {
                context.startActivity(Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }

            SplashNavigation.Home -> {
                context.startActivity(Intent(context, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = splashColor)
                .padding(innerPadding)
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(2.3f))
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.splashlogo), contentDescription = "splash")
                Spacer(modifier = Modifier.weight(7f))
            }
        }
    }
}