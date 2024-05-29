package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.example.colyak.model.data.RegisterData
import com.example.colyak.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(navController: NavController) {
    val registerVM: RegisterViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val email = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordRepeat = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val responseState = remember { mutableStateOf(false) }
    var isPassword by remember { mutableStateOf(true) }
    var isPasswordRepeat by remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf("") }

    Scaffold(
        content = { padding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.colyak),
                    contentDescription = "",
                    Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.size(height = 8.dp, width = 0.dp))
                Input(
                    tfValue = email.value,
                    onValueChange = { email.value = it },
                    label = "E-posta",
                    isPassword = false,
                    leadingIcon = R.drawable.email
                )
                Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                Input(
                    tfValue = username.value,
                    onValueChange = { username.value = it },
                    label = "Kullanıcı Adı",
                    isPassword = false,
                    leadingIcon = R.drawable.account_circle
                )
                Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                Input(
                    tfValue = password.value,
                    onValueChange = { password.value = it },
                    label = "Şifre",
                    isPassword = isPassword,
                    leadingIcon = R.drawable.lock,
                    trailingIcon = if (isPassword) R.drawable.visibility else R.drawable.visibility_off,
                    trailingIconClick = {
                        isPassword = !isPassword
                    }
                )
                Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                Input(
                    tfValue = passwordRepeat.value,
                    onValueChange = { passwordRepeat.value = it },
                    label = "Şifre Tekrar",
                    isPassword = isPasswordRepeat,
                    leadingIcon = R.drawable.lock,
                    trailingIcon = if (isPasswordRepeat) R.drawable.visibility else R.drawable.visibility_off,
                    trailingIconClick = {
                        isPasswordRepeat = !isPasswordRepeat
                    }
                )
                Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                CustomizeButton(
                    onClick = {
                        scope.launch {
                            when {
                                password.value.length < 8 -> {
                                    responseState.value = false
                                    errorMessage.value = "Şifre en az 8 karakter olmalıdır."
                                    snackbarHostState.showSnackbar(
                                        "",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                password.value != passwordRepeat.value -> {
                                    responseState.value = false
                                    errorMessage.value = "Şifreler eşleşmiyor."
                                    snackbarHostState.showSnackbar(
                                        "",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                else -> {
                                    val response = registerVM.register(
                                        RegisterData(
                                            email.value,
                                            username.value,
                                            password.value
                                        ),
                                    )
                                    responseState.value = response
                                    if (responseState.value) {
                                        snackbarHostState.showSnackbar(
                                            "",
                                            duration = SnackbarDuration.Short
                                        )
                                        navController.navigate(Screens.VerificationScreen.screen)
                                    } else {
                                        errorMessage.value = "Kullanıcı oluşturulurken hata."
                                        snackbarHostState.showSnackbar(
                                            "",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                        }
                    },
                    buttonText = "Kayıt Ol",
                    backgroundColor = colorResource(id = R.color.statusBarColor)
                )
                Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Zaten bir hesabın var mı ? ", fontSize = 18.sp)
                    Text(
                        text = "Giriş Yap",
                        fontWeight = FontWeight.W400,
                        fontSize = 18.sp,
                        color = colorResource(
                            id = R.color.statusBarColor
                        ),
                        modifier = Modifier.clickable {
                            navController.navigate(Screens.Login.screen)
                        }
                    )
                }
            }
        },
        snackbarHost = {
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
                            text = if (responseState.value) {
                                "Kullanıcı oluşturuldu doğrulama ekranına yönlendiriliyorsunuz."
                            } else {
                                errorMessage.value
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W400
                        )
                    }
                }
            }
        }
    )
}
