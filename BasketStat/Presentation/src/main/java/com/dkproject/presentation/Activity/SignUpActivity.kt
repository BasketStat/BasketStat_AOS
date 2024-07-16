package com.dkproject.presentation.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dkproject.presentation.navigation.SignUpNavigation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpNavigation()
        }
    }
}