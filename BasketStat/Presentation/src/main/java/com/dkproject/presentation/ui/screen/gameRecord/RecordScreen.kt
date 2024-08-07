package com.dkproject.presentation.ui.screen.gameRecord

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dkproject.presentation.R
import com.dkproject.presentation.ui.theme.BasketStatTheme
import com.dkproject.presentation.ui.theme.background
import com.dkproject.presentation.ui.theme.recordColor1
import com.dkproject.presentation.ui.theme.recordColor2
import com.dkproject.presentation.ui.theme.recordboxborder


@Composable
fun RecordScreen(

) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = background)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            imageVector = ImageVector.vectorResource(id = R.drawable.recordlogo),
            contentDescription = null
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.clickable {  },
                text = "1Q",
                fontSize = 52.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(0.6f))

            RedTeamBox(modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            recordColor2,
                            recordColor1
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
                .border(1.dp, recordboxborder, RoundedCornerShape(4.dp))
                .fillMaxWidth(0.9f)
                .weight(1f)
            )



            Spacer(modifier = Modifier.height(24.dp))

            BlueTeamBox(modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            recordColor2,
                            recordColor1
                        ),
                        start = Offset(0f, Float.POSITIVE_INFINITY),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
                .border(1.dp, recordboxborder, RoundedCornerShape(4.dp))
                .fillMaxWidth(0.9f)
                .weight(1f)
            )

            Spacer(modifier = Modifier.weight(3f))
        }
    }
}

@Composable
fun RedTeamBox(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {

    }
}

@Composable
fun BlueTeamBox(
    modifier: Modifier = Modifier
){
    Box(modifier = modifier) {

    }
}

@Composable
@Preview(showBackground = true)
private fun RecordScreenPreview() {
    BasketStatTheme {
        RecordScreen()
    }
}