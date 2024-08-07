package com.dkproject.presentation.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dkproject.presentation.navigation.GameRecordNavigation
import com.dkproject.presentation.ui.screen.gameRecord.RecordScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameRecordNavigation(onBack = {
                finish()
            }) {user->
                val result = Intent().apply {
                    putExtra("User",user)
                }
                setResult(Activity.RESULT_OK,result)
                finish()
            }
        }
    }
}