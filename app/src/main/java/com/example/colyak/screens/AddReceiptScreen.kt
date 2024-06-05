package com.example.colyak.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
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
                    receipt.nutritionalValuesList?.get(selectedButtonIndex.intValue)?.let {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            Text(text = "Kalori")
                            Text(text = it.calorieAmount.toString())
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
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                    ) {
                        val typeList = mutableListOf<String>()
                        receipt.nutritionalValuesList?.forEach { receipt ->
                            if (receipt != null) {
                                receipt.type?.let { typeList.add(it) }
                                Log.e("typeList", typeList.toString())
                            }
                        }
                        val pairedTypeList = typeList.chunked(2)
                        pairedTypeList.forEach { pairedTypes ->
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                pairedTypes.forEach { receiptAmount ->
                                    val isSelected = selectedButtonIndex.value == typeList.indexOf(receiptAmount)
                                    Button(
                                        onClick = {
                                            selectedButtonIndex.value = typeList.indexOf(receiptAmount)
                                            amountType.value =
                                                if (selectedButtonIndex.value != -1) receipt.nutritionalValuesList?.get(
                                                    selectedButtonIndex.value
                                                )
                                                    ?.toString()!! else ""
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) colorResource(id = R.color.appBarColor) else Color.LightGray,
                                            contentColor = if (isSelected) Color.White else colorResource(
                                                id = R.color.appBarColor
                                            )
                                        ),
                                        modifier = Modifier.padding(horizontal = 4.dp).weight(1f)
                                    ) {
                                        Text(text = receiptAmount)
                                    }
                                }
                            }
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
                                    textFieldHeight =
                                        with(density) { size.height.toDp().value.toInt() }
                                }
                                .padding(vertical = 4.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
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
                                    foodList.add(
                                        it
                                    )
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
                                Toast.makeText(
                                    ColyakApp.applicationContext(),
                                    "Ekleme Başarılı",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    enabled = !receipt.nutritionalValuesList.isNullOrEmpty(),
                    buttonText = "Ekle",
                    backgroundColor = colorResource(id = R.color.appBarColor)
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(25.dp))
}