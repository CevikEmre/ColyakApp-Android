package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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

@SuppressLint("UnrememberedMutableState")
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val tfValue = remember { mutableStateOf("") }
    var showAlert by mutableStateOf(false)
    val forgotPasswordVM: ForgotPasswordViewModel = viewModel()
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f),
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
                    showAlert = true
                    forgotPasswordVM.forgotPassword(tfValue.value)
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
                            id = R.color.appBarColor
                        )
                    )
                }
            )
        }
    }
}
