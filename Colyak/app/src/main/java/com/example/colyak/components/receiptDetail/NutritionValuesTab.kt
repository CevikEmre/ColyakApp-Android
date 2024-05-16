package com.example.colyak.components.receiptDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.colyak.model.Receipt


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
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                receipt.nutritionalValuesList?.let { nutritionalValuesList ->
                    items(nutritionalValuesList.size) {
                        val nutritionalValues = nutritionalValuesList[it]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 12.dp),
                            elevation = CardDefaults.cardElevation(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF1EC),
                            )
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    if (nutritionalValues != null) {
                                        nutritionalValues.type?.let { it1 ->
                                            Text(
                                                text = it1,
                                                Modifier.padding(vertical = 10.dp),
                                                fontWeight = FontWeight.W500,

                                            )
                                        }
                                    } else Text(text = "")
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
                                    Text(text = "Kalori " )
                                    Text(text = nutritionalValues.calorieAmount.toString())
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
                                    Text(text = nutritionalValues.carbohydrateAmount.toString())
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
                                    Text(text = nutritionalValues.proteinAmount.toString())
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
                                    Text(text = nutritionalValues.fatAmount.toString())
                                } else Text(text = "")
                            }
                    }
                }
            }
        }
    }
}


