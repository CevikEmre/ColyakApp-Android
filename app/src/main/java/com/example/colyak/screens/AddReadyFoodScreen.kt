package com.example.colyak.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.model.FoodList
import com.example.colyak.model.PrintedMeal
import com.example.colyak.model.ReadyFoods
import com.example.colyak.model.enum.FoodType
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddReadyFoodScreen(
    readyFoods: ReadyFoods,
) {
    val selectedButtonIndex = remember { mutableIntStateOf(0) }
    val tfAmount = remember { mutableIntStateOf(1) }
    val verticalScrollState = rememberScrollState()
    val printedMealList = remember { mutableStateOf(emptyList<PrintedMeal>()) }
    val foodList = remember { mutableStateListOf<FoodList>() }
    val amountType = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var textFieldHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    Box(modifier = Modifier.wrapContentSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                readyFoods.name?.let { name ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = name,
                            fontSize = 18.sp
                        )
                    }
                }
                HorizontalDivider(thickness = 1.dp)
                Card(
                    elevation = CardDefaults.cardElevation(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        readyFoods.nutritionalValuesList?.get(selectedButtonIndex.intValue)?.let {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Text(text = "Kalori")
                                Text(text = it.calorieAmount.toString(), fontWeight = FontWeight.W500)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Text(text = "Karbonhidrat")
                                Text(text = it.carbohydrateAmount.toString())
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Text(text = "Protein")
                                Text(text = it.proteinAmount.toString())
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Text(text = "Yağ")
                                Text(text = it.fatAmount.toString())
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                                .fillMaxWidth()
                        ) {
                            val unitList = mutableListOf<String>()
                            readyFoods.nutritionalValuesList?.forEach { unit ->
                                if (unit != null) {
                                    unit.type?.let { unitList.add(it) }
                                }
                            }
                            items(unitList.size) { index ->
                                val isSelected = selectedButtonIndex.intValue == index
                                Button(
                                    onClick = {
                                        selectedButtonIndex.intValue = index
                                        amountType.value =
                                            if (selectedButtonIndex.intValue != -1) readyFoods.nutritionalValuesList?.get(
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
                                    Text(text = unitList[index])
                                }
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier.padding(5.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = {
                                tfAmount.intValue -= 1
                            }
                        ) {
                            Text(text = "-", fontSize = 20.sp)
                        }
                        BasicTextField(
                            value = tfAmount.intValue.toString(),
                            onValueChange = { newText ->
                                if (newText.isEmpty()) {
                                    tfAmount.intValue = 0
                                } else if (newText.all { it.isDigit() }) {
                                    tfAmount.intValue = newText.toInt()
                                }
                            },
                            modifier = Modifier
                                .width(50.dp)
                                .onSizeChanged { size ->
                                    textFieldHeight =
                                        with(density) { size.height.toDp().value.toInt() }
                                }
                                .padding(vertical = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                        TextButton(
                            onClick = {
                                tfAmount.intValue += 1
                            }
                        ) {
                            Text(text = "+", fontSize = 18.sp)
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
                Spacer(modifier = Modifier.height(25.dp))
                CustomizeButton(
                    onClick = {
                        scope.launch {
                            val lastCarbAmount: Int =
                                (tfAmount.intValue.toDouble() * (readyFoods.nutritionalValuesList?.get(
                                    selectedButtonIndex.intValue
                                )?.carbohydrateAmount!!)).toInt()
                            foodList.add(
                                FoodList(
                                    foodType = FoodType.BARCODE,
                                    foodId = readyFoods.id,
                                    carbonhydrate = lastCarbAmount.toLong()
                                )
                            )
                            printedMealList.value += PrintedMeal(
                                mealName = readyFoods.name!!,
                                amount = tfAmount.intValue,
                                unit = amountType.value,
                                carb = lastCarbAmount
                            )
                            eatenMealList += printedMealList.value
                            bolusList += foodList
                            printedMealList.value = emptyList()
                            foodList.removeAll(foodList)
                            isVisibleReadyFood.value = false
                            visible.value = true
                            Toast.makeText(ColyakApp.applicationContext(), "Ekleme Başarılı", Toast.LENGTH_SHORT).show()
                        }
                    },
                    buttonText = "Ekle",
                    enabled = !readyFoods.nutritionalValuesList.isNullOrEmpty(),
                    backgroundColor = colorResource(id = R.color.appBarColor)
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(30.dp))
}