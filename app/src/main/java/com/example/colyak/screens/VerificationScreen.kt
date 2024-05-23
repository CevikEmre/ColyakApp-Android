package com.example.colyak.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.components.consts.Input
import com.example.colyak.model.data.VerificationData
import com.example.colyak.viewmodel.RegisterViewModel
import com.example.colyak.viewmodel.VerificationViewModel
import com.example.colyak.viewmodel.verificationId
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VerificationScreen(navController: NavController) {
    var tfvalue by remember { mutableStateOf("") }
    val verificationVM: VerificationViewModel = viewModel()
    val registerVM: RegisterViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val responseState = remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Doğrulama Ekranı"
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Spacer(modifier = Modifier.size(height = 12.dp, width = 0.dp))
                Text(text = "Lütfen E-maile gelen kodu girin")
                Input(
                    tfValue = tfvalue,
                    onValueChange = { tfvalue = it },
                    label = "Emaile gelen kodu girin",
                    isPassword = false,
                    leadingIcon = R.drawable.email
                )
                CustomizeButton(
                    onClick = {
                        scope.launch {
                            val response = verificationVM.verification(
                                VerificationData(
                                    verificationId,
                                    tfvalue
                                )
                            )
                            Log.e(
                                "verify", response.toString()
                            )
                            responseState.value = response
                            if (responseState.value) {
                                snackbarHostState.showSnackbar("")
                                navController.navigate(Screens.Login.screen)
                            } else {
                                snackbarHostState.showSnackbar("")
                            }
                        }
                    },
                    buttonText = "Gönder",
                    backgroundColor = colorResource(id = R.color.statusBarColor)
                )
            }
        },
        snackbarHost = {
            if (responseState.value) {
                SnackbarHost(hostState = snackbarHostState) {
                    Snackbar(
                        contentColor = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Kod Doğrulama Başarılı. Giriş Ekranına Yönlendiriliyorsunuz Lütfen Bekleyin",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400
                            )
                        }

                    }
                }
            } else {
                SnackbarHost(hostState = snackbarHostState) {
                    Snackbar(
                        contentColor = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Doğrulama Kodu Hatalı Lütfen Tekrar Deneyin",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                    }
                }
            }
        }
    )


}