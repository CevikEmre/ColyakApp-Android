package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.components.consts.Input
import com.example.colyak.model.data.SuggestionData
import com.example.colyak.viewmodel.SuggestionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SuggestionScreen(navController: NavController) {
    val tfValue = remember { mutableStateOf(readedBarcode) }
    var showAlert by mutableStateOf(false)
    val scope = rememberCoroutineScope()
    val suggestionVM: SuggestionViewModel = viewModel()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Öneri Sayfası")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.Black
                ),
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.size(height = 25.dp, width = 0.dp))
                Text(
                    text = "Uyguluma hakkındaki önerilerinizi iletebilirsiniz",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.size(height = 18.dp, width = 0.dp))
                Input(
                    tfValue = tfValue.value,
                    onValueChange = { tfValue.value = it },
                    label = "Öneri",
                    isPassword = false,
                    leadingIcon = R.drawable.suggestion
                )
                Spacer(modifier = Modifier.size(height = 12.dp, width = 0.dp))
                CustomizeButton(
                    onClick = {
                        scope.launch {
                            suggestionVM.addSuggestion(SuggestionData(tfValue.value))
                            showAlert = true
                        }
                    }, buttonText = "Gönder",
                    backgroundColor = colorResource(id = R.color.statusBarColor)
                )
                if (showAlert) {
                    AlertDialog(
                        onDismissRequest = {
                            showAlert = false
                        },
                        title = { Text("Öneriniz Alındı") },
                        text = { Text("Öneriniz alındı. Teşekkür ederiz") },
                        confirmButton = {
                            CustomizeButton(
                                onClick = {
                                    showAlert = false
                                    navController.navigate(Screens.MainScreen.screen)
                                }, buttonText = "Tamam", backgroundColor = colorResource(
                                    id = R.color.statusBarColor
                                )
                            )
                        }
                    )
                }
            }
        }
    )
}