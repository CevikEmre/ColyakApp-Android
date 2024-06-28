package com.example.colyak.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colyak.model.OnBoardPage

@Composable
fun OnBoardScreenContent(onBoardPage: OnBoardPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2.8f / 4f)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF000000), Color(0xFFFF7A37)),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height)
                    ),
                    size = size
                )
            }
            Image(
                painter = painterResource(id = onBoardPage.image),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp)
            )
        }
        Text(
            text = onBoardPage.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center
        )
        Text(
            text = onBoardPage.description,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}