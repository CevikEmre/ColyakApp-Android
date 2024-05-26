package com.example.colyak.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.colyak.components.CircularIndeterminateProgressBar
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.components.consts.Input
import com.example.colyak.session.SessionManager
import com.example.colyak.viewmodel.LoginViewModel
import com.example.colyak.viewmodel.loginResponse
import kotlinx.coroutines.launch

@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter",
    "SuspiciousIndentation",
    "CoroutineCreationDuringComposition", "UnrememberedMutableState"
)
@Composable
fun LoginScreen(navController: NavController) {
    val loginScreenViewModel: LoginViewModel = viewModel()
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var showAlert by remember { mutableStateOf(false) }
    var isPassword by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val sessionManager = SessionManager(context = context)
    val loading by loginScreenViewModel.loading.collectAsState()
    var checkToken by remember { mutableStateOf(false) }
    var tokenChecked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        checkToken = loginScreenViewModel.checkToken(context)
        if (checkToken) {
            val token = sessionManager.getToken()
            val refreshToken = sessionManager.getRefreshToken()
            val userName = sessionManager.getUserName()
            if (token != null) {
                loginResponse.token = token
                loginResponse.refreshToken = refreshToken.toString()
                loginResponse.userName = userName.toString()
                navController.navigate(Screens.MainScreen.screen)
            } else {
                clearLoginResponse()
                checkToken = false
            }
        }
        tokenChecked = true
    }

    if (!tokenChecked) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.colyak),
                contentDescription = "",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.size(height = 18.dp, width = 0.dp))
            CircularIndeterminateProgressBar(isDisplay = loading)
        }
    } else if (checkToken) {
        navController.navigate(Screens.MainScreen.screen)
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.colyak),
                contentDescription = "",
                modifier = Modifier.size(250.dp)
            )
            Text(text = "Hoşgeldiniz", fontSize = 18.sp, fontWeight = FontWeight.W600)
            Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
            Input(
                tfValue = emailState.value,
                onValueChange = { emailState.value = it },
                label = "E-posta",
                isPassword = false,
                leadingIcon = R.drawable.email
            )
            Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
            Input(
                tfValue = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = "Şifre",
                isPassword = isPassword,
                leadingIcon = R.drawable.lock,
                trailingIcon = if (isPassword) R.drawable.visibility else R.drawable.visibility_off,
                trailingIconClick = {
                    isPassword = !isPassword
                }
            )
            Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
            Text(
                text = "Şifreni mi unuttun",
                fontSize = 18.sp,
                fontWeight = FontWeight.W600,
                color = Color(0xFFADA4A5),
                modifier = Modifier.clickable {
                    navController.navigate(Screens.ForgotPasswordScreen.screen)
                }
            )
            Spacer(modifier = Modifier.size(height = 15.dp, width = 0.dp))
            Button(
                onClick = {
                    val email = emailState.value
                    val password = passwordState.value
                    scope.launch {
                        val response = loginScreenViewModel.login(email = email, password)
                        if (response) {
                            scope.launch {
                                sessionManager.saveToken(
                                    token = loginResponse.token,
                                    refreshToken = loginResponse.refreshToken,
                                    userName = loginResponse.userName
                                )
                                navController.navigate(Screens.MainScreen.screen)
                                sessionManager.saveEmail(emailState.value)
                            }
                        } else {
                            showAlert = true
                        }
                        Log.e("RESPONSE", response.toString())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.statusBarColor),
                    contentColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_login_24),
                        contentDescription = ""
                    )
                    Spacer(
                        modifier = Modifier.size(
                            width = 8.dp,
                            height = 0.dp
                        )
                    )
                    Text(text = "Giriş Yap", fontSize = 18.sp, fontWeight = FontWeight.W600, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.size(width = 15.dp, height = 0.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Henüz bir hesabınız yok mu ? ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.Black
                )
                Text(
                    text = "Kayıt Ol",
                    color = colorResource(id = R.color.appBarColor),
                    modifier = Modifier.clickable {
                        navController.navigate(Screens.Register.screen)
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W400
                )
            }

            if (showAlert) {
                AlertDialog(
                    onDismissRequest = {
                        showAlert = false
                    },
                    title = { Text("Giriş Başarısız") },
                    text = { Text("Geçersiz Kullanıcı Adı veya Şifre") },
                    confirmButton = {
                        CustomizeButton(
                            onClick = {
                                showAlert = false
                            }, buttonText = "Tamam", backgroundColor = colorResource(
                                id = R.color.appBarColor
                            )
                        )
                    }
                )
            }
        }
    }
}

private fun clearLoginResponse() {
    loginResponse.token = ""
    loginResponse.refreshToken = ""
    loginResponse.userName = ""
}
