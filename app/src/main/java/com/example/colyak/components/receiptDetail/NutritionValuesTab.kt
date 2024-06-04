package com.example.colyak.components.receiptDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.colyak.R
import com.example.colyak.model.Receipt
import kotlin.math.roundToInt


@Composable
fun NutritionValuesTab(paddingValues: PaddingValues, receipt: Receipt) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = CardDefaults.cardElevation(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            if (receipt.nutritionalValuesList?.isEmpty() == true) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Besin değerleri eklenmemiştir",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                }
            }
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                receipt.nutritionalValuesList?.let { nutritionalValuesList ->
                    items(nutritionalValuesList.size) {
                        val nutritionalValues = nutritionalValuesList[it]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF1EC),
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (nutritionalValues != null) {
                                    nutritionalValues.type?.let { it1 ->
                                        Box(
                                            modifier = Modifier
                                                .size(width = 3.dp, height = 40.dp)
                                                .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 100.dp, bottomEnd = 100.dp, bottomStart = 0.dp))
                                                .background(colorResource(id = R.color.statusBarColor))
                                        )
                                        Text(
                                            text = it1,
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            fontWeight = FontWeight.W600,
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(width = 3.dp, height = 40.dp)
                                                .clip(RoundedCornerShape(topStart = 100.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 100.dp))
                                                .background(colorResource(id = R.color.statusBarColor))
                                        )
                                    }
                                } else {
                                    Text(text = "")
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (nutritionalValues != null) {
                                Text(text = "Kalori ")
                                Text(
                                    text = nutritionalValues.calorieAmount?.roundToInt().toString()
                                )
                            } else Text(text = "")
                        }
                        HorizontalDivider(thickness = 1.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (nutritionalValues != null) {
                                Text(text = "Karbonhidrat  ")
                                Text(
                                    text = nutritionalValues.carbohydrateAmount?.roundToInt()
                                        .toString()
                                )
                            } else Text(text = "")
                        }
                        HorizontalDivider(thickness = 1.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (nutritionalValues != null) {
                                Text(text = "Protein ")
                                Text(
                                    text = nutritionalValues.proteinAmount?.roundToInt().toString()
                                )
                            } else Text(text = "")
                        }
                        HorizontalDivider(thickness = 1.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (nutritionalValues != null) {
                                Text(text = "Yağ ")
                                Text(text = nutritionalValues.fatAmount?.roundToInt().toString())
                            } else Text(text = "")
                        }
                    }
                }
            }
        }
    }
}


