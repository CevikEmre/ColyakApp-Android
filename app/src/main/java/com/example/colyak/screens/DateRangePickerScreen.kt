package com.example.colyak.screens

import SessionManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.viewmodel.BolusReportViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerScreen(navController: NavController) {
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    val state = rememberDateRangePickerState()
    val (startDate, setStartDate) = remember { mutableStateOf<LocalDate?>(null) }
    val (endDate, setEndDate) = remember { mutableStateOf<LocalDate?>(null) }
    val sessionManager = SessionManager(LocalContext.current)
    val bolusReportVM: BolusReportViewModel = viewModel()


    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))
    }

    SnackbarHost(hostState = snackState, Modifier.zIndex(1f))
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DatePickerDefaults.colors().containerColor)
                .padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(painter = painterResource(id = R.drawable.arrow_back), contentDescription = "")
            }
            TextButton(
                onClick = {
                    snackScope.launch {
                        val start = state.selectedStartDateMillis?.toLocalDate()
                        val end = state.selectedEndDateMillis?.toLocalDate()
                        if (start != null && end != null) {
                            val email = sessionManager.getEmail()
                            setStartDate(start)
                            setEndDate(end)
                            email?.let { bolusReportVM.getBolusReports(it, start, end) }
                            snackState.showSnackbar("Tarihler belirlendi rapor sayfasına yönlendiriliyorsunuz")
                            navController.navigate(Screens.MealReportScreen.screen)
                        }
                    }
                },
                enabled = state.selectedEndDateMillis != null
            ) {
                Text(text = "İleri", color = colorResource(id = R.color.statusBarColor))
            }
        }
        DateRangePicker(
            state = state, modifier = Modifier.weight(1f), colors = DatePickerDefaults.colors(
                todayDateBorderColor = colorResource(id = R.color.statusBarColor),
                todayContentColor = colorResource(id = R.color.statusBarColor),
                selectedDayContainerColor = colorResource(id = R.color.statusBarColor),
                dayInSelectionRangeContainerColor = Color(0xFFECECEC),
                dateTextFieldColors = TextFieldDefaults.colors(
                    focusedIndicatorColor = colorResource(id = R.color.statusBarColor),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedLabelColor = colorResource(id = R.color.statusBarColor),
                )
            )
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
