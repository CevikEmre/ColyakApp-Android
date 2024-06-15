package com.example.colyak.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReceiptCard(
    cardName: String?,
    modifier: Modifier,
    image: @Composable () -> Unit,
    iconButton: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .height(250.dp)
            .width(200.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                image()
                iconButton?.let {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        it()
                    }
                }
            }
            cardName?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(all = 6.dp)
                )
            }
        }
    }
}
