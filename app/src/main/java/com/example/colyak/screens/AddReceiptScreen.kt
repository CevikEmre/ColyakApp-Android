@file:Suppress("NAME_SHADOWING")

package com.example.colyak.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.model.FoodList
import com.example.colyak.model.PrintedMeal
import com.example.colyak.model.Receipt
import com.example.colyak.model.enum.FoodType
import kotlinx.coroutines.launch

var bolusList = mutableStateListOf<FoodList>()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddReceiptScreen(
    receipt: Receipt,
) {
    val selectedButtonIndex = remember { mutableIntStateOf(0) }
    val tfAmount = remember { mutableIntStateOf(1) }
    val printedMealList = remember { mutableStateOf(emptyList<PrintedMeal>()) }
    val foodList = remember { mutableStateListOf<FoodList>() }
    val amountType = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var textFieldHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val typeList = mutableListOf<String>()
    receipt.nutritionalValuesList?.forEach { receipt ->
        if (receipt != null) {
            receipt.type?.let { typeList.add(it) }
            Log.e("typeList", typeList.toString())
        }
    }
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(typeList.firstOrNull() ?: "") }

    Box(modifier = Modifier.wrapContentSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 12.dp)
            ) {
                receipt.receiptName?.let {
                    Text(
                        text = it,
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
                ) {
                    receipt.nutritionalValuesList?.get(selectedButtonIndex.intValue)?.let { receipt ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Kalori")
                            Text(text = (tfAmount.intValue.toDouble() * receipt.calorieAmount!!).toString())
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Karbonhidrat")
                            Text(text = (tfAmount.intValue.toDouble() * receipt.carbohydrateAmount!!).toString())
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Protein")
                            Text(text = (tfAmount.intValue.toDouble() * receipt.proteinAmount!!).toString())
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Yağ")
                            Text(text = (tfAmount.intValue.toDouble() * receipt.fatAmount!!).toString())
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Ölçü birimi seçiniz")
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .border(0.5.dp, Color.Gray)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = { expanded = true },
                        ) {
                            Text(text = selectedType)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = ""
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        typeList.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(text = type) }, onClick = {
                                    selectedType = type
                                    selectedButtonIndex.intValue = typeList.indexOf(type)
                                    amountType.value =
                                        receipt.nutritionalValuesList?.get(selectedButtonIndex.intValue)
                                            ?.toString() ?: ""
                                    expanded = false

                                })

                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
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
                                    textFieldHeight = with(density) { size.height.toDp().value.toInt() }
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
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(25.dp))
                CustomizeButton(
                    onClick = {
                        scope.launch {
                            val selectedTypeIndex = selectedButtonIndex.intValue
                            if (selectedTypeIndex != -1) {
                                val selectedType =
                                    receipt.nutritionalValuesList?.get(selectedTypeIndex)
                                val calculatedCarbohydrate =
                                    ((tfAmount.intValue.toDouble()) * selectedType!!.carbohydrateAmount!!).toInt()

                                receipt.id?.let {
                                    FoodList(
                                        foodType = FoodType.RECEIPT,
                                        foodId = it,
                                        carbonhydrate = calculatedCarbohydrate.toLong()
                                    )
                                }?.let {
                                    foodList.add(it)
                                }
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
                                isVisibleReceipt.value = false
                                visible.value = true
                                Toast.makeText(
                                    ColyakApp.applicationContext(), "Ekleme Başarılı", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    enabled = !receipt.nutritionalValuesList.isNullOrEmpty(),
                    buttonText = "Ekle",
                    backgroundColor = colorResource(id = R.color.statusBarColor)
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(25.dp))
}
