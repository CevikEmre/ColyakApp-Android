package com.example.colyak.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.model.MealDetail
import com.example.colyak.model.PrintedMeal

var eatenMealList = mutableStateListOf<PrintedMeal>()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(mealDetail: MealDetail, navController: NavController) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("${Screens.AddMealScreen.screen}/${mealDetail.mealName}")
                }, containerColor = colorResource(
                    id = R.color.statusBarColor
                )
            ) {
                Icon(Icons.Sharp.Add, contentDescription = "", tint = Color.White)
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "${mealDetail.mealName} Detayı", color = Color.Black)
                },
                modifier = Modifier.shadow(10.dp),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.Black,
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.Black
                ),
            )

        },
        content = { padding ->
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(padding)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        elevation = CardDefaults.cardElevation(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFF1EC),
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 3.dp, height = 40.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 0.dp,
                                            topEnd = 100.dp,
                                            bottomEnd = 100.dp,
                                            bottomStart = 0.dp
                                        )
                                    )
                                    .background(colorResource(id = R.color.statusBarColor))
                            )
                            Text(
                                text = "Karbonhidrat",
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(70.dp))
                            Text(
                                text = "${calculateTotalCarbs(eatenMealList)} Gr",
                                fontWeight = FontWeight.W600,
                                fontSize = 18.sp
                            )
                            Box(
                                modifier = Modifier
                                    .size(width = 3.dp, height = 40.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 100.dp,
                                            topEnd = 0.dp,
                                            bottomEnd = 0.dp,
                                            bottomStart = 100.dp
                                        )
                                    )
                                    .background(colorResource(id = R.color.statusBarColor))
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                if (eatenMealList.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CustomizeButton(onClick = { navController.navigate(Screens.BolusScreen.screen) }, buttonText = "Bolus Hesapla")
                    }
                }
                Text(
                    text = mealDetail.mealName,
                    Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600
                )
                Spacer(modifier = Modifier.width(10.dp))
                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                ) {
                    items(eatenMealList.size) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 5.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 2.dp, top = 4.dp, end = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        when (mealDetail.mealName) {
                                            "Öğün" -> eatenMealList[it].mealName?.let { it1 ->
                                                Text(
                                                    text = it1,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.W600,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Visible,
                                                    modifier = Modifier.padding(bottom = 5.dp)
                                                )
                                            }
                                        }
                                        when (mealDetail.mealName) {
                                            "Öğün" -> Text(
                                                text = "Karbonhidrat : ${eatenMealList[it].carb} Gram",
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            eatenMealList.removeAt(it)
                                            bolusList.removeAt(it)
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.delete),
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 18.dp),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    )
}

fun calculateTotalCarbs(printedMeals: List<PrintedMeal>): Int {
    var totalCarbs = 0
    for (meal in printedMeals) {
        totalCarbs += meal.carb
    }
    return totalCarbs
}