package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.functions.SearchTextField
import com.example.colyak.model.PrintedMeal
import com.example.colyak.viewmodel.ReceiptViewModel
import com.google.gson.Gson

var typeList = listOf("Tarif", "Hazır Yemek")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(mealName: String, navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "$mealName Ekle")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(padding)
                    .padding(3.dp)
                    .fillMaxSize()
            ) {
                AddMealToList(navController)
            }
        }
    )
}

var eatenMealList = mutableStateListOf<PrintedMeal>()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddMealToList(navController: NavController) {
    val receiptScreenViewModel: ReceiptViewModel = viewModel()
    val receiptList by receiptScreenViewModel.filteredReceiptList.collectAsState()
    val selectedTypeIndex = remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        Card(Modifier.padding(5.dp)) {
            SearchTextField { query ->
                searchQuery = query
            }
        }
        TabRow(
            selectedTabIndex = selectedTypeIndex.intValue,
            contentColor = colorResource(id = R.color.appBarColor),
            modifier = Modifier.background(Color.White),
            containerColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTypeIndex.intValue]),
                    color = colorResource(id = R.color.appBarColor)
                )
            }
        ) {
            typeList.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            title,
                            fontSize = 15.sp
                        )
                    },
                    selected = selectedTypeIndex.intValue == index,
                    onClick = {
                        selectedTypeIndex.intValue = index
                    }
                )
            }
        }

        when (selectedTypeIndex.intValue) {
            0 -> {
                val filteredReceiptList =
                    receiptList?.filter { it?.receiptName!!.contains(searchQuery, ignoreCase = true) }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (filteredReceiptList != null) {
                        items(filteredReceiptList.size) {
                            val choose = filteredReceiptList[it]
                            Card(
                                elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                                    .clickable {
                                        val chooseJson = Gson().toJson(choose)
                                        navController.navigate("${Screens.AddReceiptScreen.screen}/$chooseJson")
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 3.dp)
                                ) {
                                    if (choose != null) {
                                        choose.receiptName?.let { it1 ->
                                            Text(
                                                text = it1,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.W500,
                                                modifier = Modifier.padding(
                                                    vertical = 6.dp,
                                                    horizontal = 4.dp
                                                )
                                            )
                                        }
                                    }
                                    Icon(
                                        painter = painterResource(id = R.drawable.arrow_right),
                                        contentDescription = "",
                                        tint = colorResource(id = R.color.appBarColor)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                val filteredReadyFoodList = globalReadyFoodList?.filter {
                    it?.name?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true
                }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (filteredReadyFoodList != null) {
                        items(filteredReadyFoodList.size) {
                            val readyFood = filteredReadyFoodList[it]
                            Card(
                                elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                                    .clickable {
                                        val readyFoodJson = Gson().toJson(readyFood)
                                        navController.navigate("${Screens.AddReadyFoodScreen.screen}/$readyFoodJson")
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 3.dp)
                                ) {
                                    if (readyFood != null) {
                                        Text(
                                            text = readyFood.name ?: "BOŞ",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.W500,
                                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                                        )
                                    }
                                    Icon(
                                        painter = painterResource(id = R.drawable.arrow_right),
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}