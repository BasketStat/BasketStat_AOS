package com.dkproject.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dkproject.presentation.ui.theme.recordboxborder


@Composable
fun HomeMenuButton(
    modifier: Modifier = Modifier,
    text: String,
    image: ImageVector? = null,
    onClick: () -> Unit
) {
    Surface(modifier = modifier, shape = RoundedCornerShape(4.dp), color = Color.Transparent, border = BorderStroke(1.dp, recordboxborder)) {
        Row(
            modifier = Modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            image?.let {
                Icon(modifier = Modifier.padding(end = 6.dp),imageVector = it, contentDescription = null)
            }
            Text(modifier=Modifier.padding(6.dp),text = text, color = Color.White, fontSize = 22.sp)
        }
    }
}