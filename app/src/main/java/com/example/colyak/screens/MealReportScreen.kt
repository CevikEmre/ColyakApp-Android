package com.example.colyak.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.CircularIndeterminateProgressBar
import com.example.colyak.components.functions.convertDateTime
import com.example.colyak.viewmodel.BolusReportViewModel
import com.example.colyak.viewmodel.mealReportList
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MealReportScreen(navController: NavController) {
    val bolusReportVM: BolusReportViewModel = viewModel()
    Log.e("MealReportScreen", mealReportList.toString())
    val isLoading by bolusReportVM.loading.collectAsState()

    if (isLoading) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularIndeterminateProgressBar(isDisplay = isLoading)
        }
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text(text = "Raporlarım") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = colorResource(id = R.color.appBarColor),
                        titleContentColor = Color.Black
                    ),
                    modifier = Modifier.shadow(10.dp),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                    }
                )
            },
            content = { padding ->
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(mealReportList.size) {
                        if (mealReportList.size == 0) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Rapor yok",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        val meal = mealReportList[it]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 2.dp, top = 4.dp, end = 2.dp)
                                .clickable {
                                    val mealJson = Gson().toJson(meal)
                                    val formattedMealJson = Uri.encode(mealJson)
                                    navController.navigate("${Screens.MealReportDetailScreen.screen}/$formattedMealJson")
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 18.dp, horizontal = 8.dp)
                            ) {
                                Text(
                                    text = convertDateTime(meal.dateTime),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_right),
                                    contentDescription = "",
                                    tint = colorResource(
                                        id = R.color.statusBarColor
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        )
    }
}


