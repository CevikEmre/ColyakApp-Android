package com.example.colyak.screens

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.components.consts.Input
import com.example.colyak.model.Bolus
import com.example.colyak.model.data.BolusData
import com.example.colyak.viewmodel.BolusScreenViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalTime
import java.util.Locale
import kotlin.math.roundToLong


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun BolusScreen() {
    val scope = rememberCoroutineScope()
    val bloodGlucose = remember { mutableStateOf("") }
    val targetBloodGlucose = remember { mutableStateOf("") }
    val carbohydrateTf = remember { mutableStateOf("") }
    var carbohydrate = calculateTotalCarbs(eatenMealList)
    val insulinCarbRatio = remember { mutableStateOf("") }
    val idf = remember { mutableStateOf("") }
    val result = remember { mutableDoubleStateOf(0.0) }
    val sheetState = rememberModalBottomSheetState()
    val isVisible = remember { mutableStateOf(false) }
    val viewModel: BolusScreenViewModel = viewModel()
    val showAlert = remember { mutableStateOf(false) }
    val verticalScroll = rememberScrollState()
    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }
    var printedEatTime by remember { mutableStateOf("") }
    var mealEatTime by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    carbohydrateTf.value = carbohydrate.toString()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Bolus Hesapla")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.White
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(12.dp)),

                )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(verticalScroll),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Öğün Saati",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                            )

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.time),
                                contentDescription = "",
                                modifier = Modifier.size(40.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showTimePicker = true
                                    }
                            ) {
                                Input(
                                    tfValue = printedEatTime,
                                    onValueChange = {},
                                    label = "Yemeği yediğiniz saat",
                                    isPassword = false,
                                    trailingIcon = R.drawable.time,
                                    trailingIconClick = {
                                        showTimePicker = true
                                    },
                                    readOnly = true,
                                    modifier = Modifier.clickable {
                                        showTimePicker = true
                                    }
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Öğünü tükkettiğiniz saat",
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Kan Şekeri",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                            )

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sugar_blood),
                                contentDescription = "",
                                modifier = Modifier.size(40.dp)
                            )
                            Input(
                                tfValue = bloodGlucose.value,
                                onValueChange = { newText ->
                                    if (newText.all { it.isDigit() }) {
                                        bloodGlucose.value = newText
                                    }
                                },
                                label = "Kan Şekeri",
                                isPassword = false,
                                keybordType = KeyboardType.Number,
                                modifier = Modifier
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Açlık Kan Şekeri",
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Hedef Kan Şekeri",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                            )

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.target),
                                contentDescription = "",
                                modifier = Modifier.size(40.dp)
                            )
                            Input(

                                tfValue = targetBloodGlucose.value,
                                onValueChange = { newText ->
                                    if (newText.all { it.isDigit() }) {
                                        targetBloodGlucose.value = newText
                                    }
                                },
                                label = "Hedef Kan Şekeri",
                                isPassword = false,
                                keybordType = KeyboardType.Number,
                                modifier = Modifier
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Doktorun Uygun Gördüğü Hedef Kan Şekeri",
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Karbonhidrat Miktarı",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                            )

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.carbohydrate),
                                contentDescription = "",
                                modifier = Modifier.size(40.dp)
                            )
                            Input(
                                tfValue = carbohydrateTf.value,
                                onValueChange = { newText ->
                                    if (newText.isEmpty()) {
                                        carbohydrate = 0
                                    } else if (newText.all { it.isDigit() }) {
                                        carbohydrateTf.value = newText
                                        carbohydrate = newText.toInt()
                                    }
                                },
                                label = "Karbonhidrat Miktarı",
                                isPassword = false,
                                keybordType = KeyboardType.Number,
                                modifier = Modifier
                            )

                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Öğünde Alınan Karbonhidrat Miktarı",
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "İnsülin / Karbonhidrat Oranı",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                            )

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.percent),
                                contentDescription = "",
                                modifier = Modifier.size(40.dp)
                            )
                            Input(

                                tfValue = insulinCarbRatio.value,
                                onValueChange = { newText ->
                                    if (newText.all { it.isDigit() }) {
                                        insulinCarbRatio.value = newText
                                    }
                                },
                                label = "İnsülin / Karbonhidrat Oranı",
                                isPassword = false,
                                keybordType = KeyboardType.Number,
                                modifier = Modifier
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "İnsülin / Karbonhidrat Oranı",
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "IDF (İnsülin Duyarlılık Faktörü)",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                            )

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.idf),
                                contentDescription = "",
                                modifier = Modifier.size(40.dp)
                            )
                            Input(
                                tfValue = idf.value,
                                onValueChange = { newText ->
                                    if (newText.all { it.isDigit() }) {
                                        idf.value = newText
                                    }
                                },
                                label = "IDF (İnsülin Duyarlılık Faktörü)",
                                isPassword = false,
                                keybordType = KeyboardType.Number,
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "IDF (İnsülin Duyarlılık Faktörü)",
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                CustomizeButton(
                    onClick = {
                        scope.launch {
                            if (carbohydrateTf.value.isEmpty() || bloodGlucose.value.isEmpty() || targetBloodGlucose.value.isEmpty() || insulinCarbRatio.value.isEmpty() || idf.value.isEmpty() || idf.value.toInt() == 0 || insulinCarbRatio.value.toInt() == 0) {
                                showAlert.value = true
                            } else {
                                result.doubleValue =
                                    ((carbohydrate.toDouble() / insulinCarbRatio.value.toDouble()) + (bloodGlucose.value.toDouble() - targetBloodGlucose.value.toDouble()) / idf.value.toDouble())
                                isVisible.value = true
                                viewModel.bolus(
                                    BolusData(
                                        bolusList,
                                        Bolus(
                                            bloodSugar = bloodGlucose.value.toLong(),
                                            targetBloodSugar = targetBloodGlucose.value.toLong(),
                                            bolusValue = result.doubleValue.roundToLong(),
                                            insulinCarbonhydrateRatio = insulinCarbRatio.value.toLong(),
                                            insulinTolerateFactor = idf.value.toLong(),
                                            totalCarbonhydrate = carbohydrate.toLong(),
                                            eatingTime = mealEatTime.toString(),
                                        )
                                    )
                                )
                                Log.e("bolusList", bolusList.toString())
                            }
                        }
                    },
                    buttonText = "Hesapla",
                    backgroundColor = colorResource(
                        id = R.color.appBarColor
                    )
                )
                Spacer(modifier = Modifier.size(height = 25.dp, width = 0.dp))
                if (isVisible.value) {
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = {
                            isVisible.value = false
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(0.18f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "Vurulacak Ünite : " + result.doubleValue.roundToLong()
                                    .toString(),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.W600,
                            )
                        }
                    }
                }
                if (showAlert.value) {
                    AlertDialog(
                        onDismissRequest = {
                            showAlert.value = false
                        },
                        title = { Text("Geçersiz Değer Uyarısı") },
                        text = { Text("Lütfen tüm değerleri doldurun.") },
                        confirmButton = {
                            CustomizeButton(
                                onClick = {
                                    showAlert.value = false
                                }, buttonText = "Tamam", backgroundColor = colorResource(
                                    id = R.color.appBarColor
                                )
                            )
                        }
                    )
                }
                if (showTimePicker) {
                    TimePickerDialog(
                        onCancel = { showTimePicker = false },
                        onConfirm = {
                            showTimePicker = false
                            printedEatTime = String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            val selectedTime =
                                LocalTime.of(timePickerState.hour, timePickerState.minute)
                            val currentDateTime =
                                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            val updatedDateTime = LocalDateTime(
                                year = currentDateTime.year,
                                monthNumber = currentDateTime.monthNumber,
                                dayOfMonth = currentDateTime.dayOfMonth,
                                hour = selectedTime.hour,
                                minute = selectedTime.minute,
                                second = 0,
                                nanosecond = 0
                            )
                            mealEatTime = updatedDateTime
                        },
                        content = {
                            TimePicker(state = timePickerState)
                        },
                        title = "Saat Seçiniz"
                    )
                }
            }

        }
    )
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text("Vazgeç") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("Tamam") }
                }
            }
        }
    }
}
