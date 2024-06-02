package com.example.colyak.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.model.FoodList
import com.example.colyak.model.PrintedMeal
import com.example.colyak.model.Receipt
import com.example.colyak.model.enum.FoodType

var bolusList = mutableStateListOf<FoodList>()

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddReceiptScreen(
    receipt: Receipt,
    navController: NavController
) {
    val selectedButtonIndex = remember { mutableIntStateOf(0) }
    val tfAmount = remember { mutableIntStateOf(1) }
    val verticalScrollState = rememberScrollState()
    val printedMealList = remember { mutableStateOf(emptyList<PrintedMeal>()) }
    val foodList = remember { mutableStateListOf<FoodList>() }
    val amountType = remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tarif Ekle",
                        fontSize = 25.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor)
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                Card(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 18.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 8.dp)
                        ) {
                            receipt.receiptName?.let {
                                Text(
                                    text = it,
                                    fontSize = 18.sp
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                ) {
                                val typeList = mutableListOf<String>()
                                receipt.nutritionalValuesList?.forEach { receipt ->
                                    if (receipt != null) {
                                        receipt.type?.let { typeList.add(it) }
                                        Log.e("typeList", typeList.toString())
                                    }
                                }
                                items(typeList.size) {
                                    val receiptAmount = typeList[it]
                                    val isSelected = selectedButtonIndex.intValue == it
                                    Button(
                                        onClick = {
                                            selectedButtonIndex.intValue = it
                                            amountType.value =
                                                if (selectedButtonIndex.intValue != -1) receipt.nutritionalValuesList?.get(
                                                    selectedButtonIndex.intValue
                                                )
                                                    ?.toString()!! else ""

                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) colorResource(id = R.color.appBarColor) else Color.LightGray,
                                            contentColor = if (isSelected) Color.White else colorResource(
                                                id = R.color.appBarColor
                                            )
                                        ),
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    ) {
                                        Text(text = receiptAmount)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(25.dp))
                        Card(
                            modifier = Modifier.padding(5.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                TextButton(
                                    onClick = {
                                        tfAmount.intValue += 1
                                    }
                                ) {
                                    Text(text = "+",fontSize = 18.sp)
                                }

                                TextField(
                                    value = tfAmount.intValue.toString(),
                                    onValueChange = { newText ->
                                        if (newText.isEmpty()) {
                                            tfAmount.intValue = 0
                                        } else if (newText.all { it.isDigit() }) {
                                            tfAmount.intValue = newText.toInt()
                                        }
                                    },
                                    modifier = Modifier.width(75.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                TextButton(
                                    onClick = {
                                        tfAmount.intValue -= 1
                                    }
                                ) {
                                    Text(text = "-", fontSize = 20.sp)
                                }
                            }
                        }

                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(verticalScrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomizeButton(
                        onClick = {
                            val selectedTypeIndex = selectedButtonIndex.intValue
                            if (selectedTypeIndex != -1) {
                                val selectedType =
                                    receipt.nutritionalValuesList?.get(selectedTypeIndex)
                                val calculatedCarbohydrate =
                                    ((tfAmount.intValue.toDouble()) * selectedType!!.carbohydrateAmount!!).toInt()

                                foodList.add(
                                    FoodList(
                                        foodType = FoodType.RECEIPT,
                                        foodId = receipt.id,
                                        carbonhydrate = calculatedCarbohydrate.toLong()
                                    )
                                )
                                printedMealList.value += PrintedMeal(
                                    mealName = receipt.receiptName,
                                    amount = tfAmount.intValue,
                                    unit = amountType.value,
                                    carb = calculatedCarbohydrate
                                )
                                eatenMealList += printedMealList.value
                                bolusList += foodList
                                printedMealList.value = emptyList()
                                foodList.removeAll(elements = foodList)
                            }
                        },
                        buttonText = "Ekle",
                        backgroundColor = colorResource(id = R.color.appBarColor)
                    )
                    Text("Liste:")

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            eatenMealList.forEach { printedMeal ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 2.dp, top = 4.dp, end = 2.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp)
                                    ) {
                                        Row {
                                            Column {
                                                printedMeal.mealName?.let {
                                                    Text(
                                                        text = it,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.W600
                                                    )
                                                }
                                                Text(
                                                    text = "Karb :${printedMeal.carb} Gram",
                                                    fontSize = 14.sp
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
        }
    )
}