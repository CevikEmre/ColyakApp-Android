package com.example.colyak.components.cards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colyak.R

@Composable
fun NutritionInfoCard(
    modifier: PaddingValues,
    caloriGoal: Int,
    caloriTaken: Int,
    carbTaken: Int,
    proteinTaken: Int,
    fatTaken: Int,
    carbGoal: Int,
    proteinGoal: Int,
    fatGoal: Int,
) {

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(modifier)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 5.dp)
        ) {
            Text(
                text = "Özet",
                fontWeight = FontWeight.Bold, fontSize = 20.sp
            )
            Text(
                text = "Detaylar",
                fontWeight = FontWeight.Bold, fontSize = 20.sp
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp), elevation = CardDefaults.cardElevation(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 35.dp, end = 35.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = caloriTaken.toString(), fontSize = 20.sp)
                    Text(text = "Alınan", fontSize = 12.sp)
                }
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .size(175.dp)
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Canvas(modifier = Modifier.size(150.dp)) {
                        drawArc(
                            color = Color(0xffE3E1D9),
                            -90f,
                            360f,
                            useCenter = false,
                            style = Stroke(24f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = Color(0xFFFF8911),
                            -90f,
                            360 * (caloriTaken.toFloat() / caloriGoal.toFloat()),
                            useCenter = false,
                            style = Stroke(18f, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "%.3f".format((caloriGoal.toFloat() - caloriTaken.toFloat()) / 1000),
                            fontSize = 24.sp
                        )
                        Text(text = "Kalan", fontSize = 16.sp)
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = caloriGoal.toString(), fontSize = 20.sp)
                    Text(text = "Hedef", fontSize = 12.sp)
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Karbonhidrat", Modifier.padding(vertical = 8.dp))
                    LinearProgressIndicator(
                        progress = (carbTaken.toFloat() / carbGoal.toFloat()),
                        Modifier
                            .size(width = 100.dp, height = 6.dp)
                            .clip(shape = RoundedCornerShape(8.dp)),
                        color = colorResource(id = R.color.appBarColor)
                    )
                    Text(text = "$carbTaken / $carbGoal g", Modifier.padding(vertical = 8.dp))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Protein", Modifier.padding(vertical = 8.dp))
                    LinearProgressIndicator(
                        progress = (proteinTaken.toFloat() / proteinGoal.toFloat()),
                        Modifier
                            .size(width = 100.dp, height = 6.dp)
                            .clip(shape = RoundedCornerShape(8.dp)),
                        color = colorResource(id = R.color.appBarColor),

                        )
                    Text(text = "$proteinTaken / $proteinGoal g", Modifier.padding(vertical = 8.dp))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Yağ", Modifier.padding(vertical = 8.dp))
                    LinearProgressIndicator(
                        progress = (fatTaken.toFloat() / fatGoal.toFloat()),
                        Modifier
                            .size(width = 100.dp, height = 6.dp)
                            .clip(shape = RoundedCornerShape(8.dp)),
                        color = colorResource(id = R.color.appBarColor)
                    )
                    Text(text = "$fatTaken / $fatGoal g", Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }

}