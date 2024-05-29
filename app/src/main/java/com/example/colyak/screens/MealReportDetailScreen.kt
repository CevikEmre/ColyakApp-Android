package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.model.BolusReport

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MealReportDetailScreen(bolusReport: BolusReport, navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Rapor Detayı")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.White
                )
            )
        },

        content = { padding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top, modifier = Modifier.padding(padding)
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp, horizontal = 12.dp),
                        elevation = CardDefaults.cardElevation(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF1EC),
                        )
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Bolus Değeri ")
                            Text(
                                text = bolusReport.bolus.bolusValue.toString(),
                                Modifier.padding(vertical = 10.dp),
                                fontWeight = FontWeight.W500,
                                )
                        }
                        HorizontalDivider(thickness = 1.dp)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Kan Şekeri")
                        Text(
                            text = bolusReport.bolus.bloodSugar.toString(),
                            fontWeight = FontWeight.W500
                        )
                    }
                    HorizontalDivider(thickness = 1.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Hedef Kan Şekeri")
                        Text(
                            text = bolusReport.bolus.targetBloodSugar.toString(),
                            fontWeight = FontWeight.W500,
                        )
                    }
                    HorizontalDivider(thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "İnsülin/Karbonhidrat Oranı")
                        Text(
                            text = bolusReport.bolus.insulinCarbonhydrateRatio.toString(),
                            fontWeight = FontWeight.W500,
                        )
                    }
                    HorizontalDivider(thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Karbonhidrat")
                        Text(
                            text = bolusReport.bolus.totalCarbonhydrate.toString(),
                            fontWeight = FontWeight.W500,
                        )
                    }
                    HorizontalDivider(thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "IDF")
                        Text(
                            text = bolusReport.bolus.insulinTolerateFactor.toString(),
                            fontWeight = FontWeight.W500,
                        )
                    }
                    HorizontalDivider(thickness = 1.dp)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Öğün Listesi")
                }
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(bolusReport.foodResponseList.size) {
                        if (bolusReport.foodResponseList.isEmpty()) {
                            Text(text = "Öğün Listesi Boş")
                        } else {
                            val z = bolusReport.foodResponseList[it]
                            Card(
                                modifier = Modifier
                                    .padding(horizontal = 6.dp, vertical = 6.dp)
                                    .wrapContentHeight(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = z.foodName,
                                            maxLines = 2,
                                            overflow = TextOverflow.Visible,
                                            modifier = Modifier.padding(bottom = 5.dp)
                                        )
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Karbonhidrat",
                                                fontWeight = FontWeight.W600
                                            )
                                            Text(text = z.carbonhydrate.toString())
                                        }
                                    }
                                    HorizontalDivider(thickness = 1.dp)
                                }
                            }
                        }
                    }
                }
            }
        }
    )

}
