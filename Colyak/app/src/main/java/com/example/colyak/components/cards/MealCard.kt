package com.example.colyak.components.cards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.colyak.R
import com.example.colyak.model.MealDetail


@Composable
fun MealCard(
    mealDetail: MealDetail,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .size(50.dp)
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Canvas(modifier = Modifier.size(150.dp)) {
                        drawArc(
                            color = Color.LightGray,
                            -90f,
                            360f,
                            useCenter = false,
                            style = Stroke(16f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = Color(0xFFFF8911),
                            -90f,
                            360 * (mealDetail.mealCaloriTaken.toFloat() / mealDetail.mealCaloriGoal.toFloat()),
                            useCenter = false,
                            style = Stroke(12f, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(mealDetail.icon),
                            contentDescription = ""
                        )
                    }
                }
                Column(Modifier.padding(start = 10.dp)) {
                    Text(text = mealDetail.mealName, fontWeight = FontWeight.W800)
                    Text(text = "${mealDetail.mealCaloriTaken} / ${mealDetail.mealCaloriGoal}")
                }
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right),
                        contentDescription = ""
                    )
                }
            }
        }
    }

}
