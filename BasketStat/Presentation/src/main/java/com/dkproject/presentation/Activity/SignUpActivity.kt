package com.dkproject.presentation.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dkproject.presentation.ui.screen.signUp.SignUpScreen
import com.dkproject.presentation.ui.theme.BasketStatTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            BasketStatTheme {
                    SignUpScreen()
            }
        }
    }
}