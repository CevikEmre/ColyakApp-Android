package com.example.colyak.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colyak.model.OnBoardPage

@Composable
fun OnBoardScrenContent(onBoardPage: OnBoardPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Image(
            painter = painterResource(id = onBoardPage.image),
            contentDescription = "",
            modifier = Modifier.height(250.dp)
        )
        Text(
            text = onBoardPage.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center
        )
        Text(
            text = onBoardPage.description,
            fontSize = 22.sp,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center
        )

    }
}