package com.dkproject.presentation.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { 
            Text(text = "HomeView")
        }
    }
}