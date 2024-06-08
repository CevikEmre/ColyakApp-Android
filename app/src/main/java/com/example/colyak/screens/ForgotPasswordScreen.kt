package com.example.colyak.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.colyak.viewmodel.ForgotPasswordViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val tfValue = remember { mutableStateOf("") }
    var showAlert by mutableStateOf(false)
    val forgotPasswordVM: ForgotPasswordViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Şifremi Unuttum") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.Black
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
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.45f)
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.size(height = 12.dp, width = 0.dp))
                Text(
                    text = "Lüften E-posta adresinizi giriniz",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600,
                )
                Input(
                    tfValue = tfValue.value,
                    onValueChange = { tfValue.value = it },
                    label = "E-posta",
                    isPassword = false,
                    leadingIcon = R.drawable.email
                )
                CustomizeButton(
                    onClick = {
                        scope.launch {
                            val result = forgotPasswordVM.forgotPassword(tfValue.value,context,navController)
                            Log.e("passwordReset",result.toString())
                        }
                    }, buttonText = "Gönder",
                    backgroundColor = colorResource(id = R.color.statusBarColor)
                )
                if (showAlert) {
                    AlertDialog(
                        onDismissRequest = {
                            showAlert = false
                        },
                        title = { Text("Kod Gönderildi") },
                        text = { Text("Lütfen E-postanıza gelen linkten şifrenizi sıfırlayın") },
                        confirmButton = {
                            CustomizeButton(
                                onClick = {
                                    showAlert = false
                                    navController.navigate(Screens.Login.screen)
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
