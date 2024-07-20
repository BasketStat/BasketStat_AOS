package com.dkproject.presentation.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dkproject.presentation.ui.screen.signUp.SignUpScreen
import com.dkproject.presentation.ui.screen.signUp.SignUpViewModel
import com.dkproject.presentation.ui.theme.BasketStatTheme
import com.dkproject.presentation.ui.theme.background
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketStatTheme {
                    SignUpScreen()
            }
        }
    }
}