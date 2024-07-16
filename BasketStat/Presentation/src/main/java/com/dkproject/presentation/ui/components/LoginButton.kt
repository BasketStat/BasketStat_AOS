package com.dkproject.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dkproject.presentation.R

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    logoImage: ImageVector,
    backgroundColor: Color,
    text: String,
    textColor: Color,
    logoWidth: Dp,
    logoHeight: Dp,
    onClick: () -> Unit
) {

    Surface(modifier=modifier.height(52.dp), shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 10.dp, bottomEnd = 0.dp),
        color = backgroundColor
    ) {
        Row(
            modifier=Modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(modifier= Modifier
                .width(logoWidth)
                .height(logoHeight), imageVector = logoImage, contentDescription = "",
                contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(24.dp))
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold,color = textColor)
        }
    }
}