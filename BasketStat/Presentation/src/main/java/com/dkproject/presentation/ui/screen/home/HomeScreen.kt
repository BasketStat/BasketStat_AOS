package com.dkproject.presentation.ui.screen.home

import android.content.Intent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dkproject.presentation.Activity.RecordActivity
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.components.HomeMenuButton
import com.dkproject.presentation.ui.theme.background

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    Scaffold() { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = background)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.3f))
                Image(
                    painter = painterResource(id = R.drawable.loginlogo), contentDescription = "",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f),
                )
                Spacer(modifier = Modifier.weight(0.1f))
                HomeMenuButton(modifier = Modifier.fillMaxWidth(0.8f),text = "팀 빌딩") {
                    context.startActivity(Intent(context, RecordActivity::class.java))
                }
                Spacer(modifier = Modifier.height(16.dp))
                HomeMenuButton(modifier = Modifier.fillMaxWidth(0.8f),text = "내 기록") {

                }
                Spacer(modifier = Modifier.height(16.dp))
                HomeMenuButton(modifier = Modifier.fillMaxWidth(0.8f),text = "설정", image = Icons.Default.Settings) {

                }


                Spacer(modifier = Modifier.weight(0.8f))
            }
        }
    }
}