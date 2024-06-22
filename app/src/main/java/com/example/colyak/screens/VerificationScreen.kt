package com.example.colyak.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.model.data.VerificationData
import com.example.colyak.viewmodel.VerificationViewModel
import com.example.colyak.viewmodel.verificationId
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(navController: NavController) {
    val verificationVM: VerificationViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val responseState = remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val code = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = List(6) { FocusRequester() }
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Doğrulama Ekranı"
                    )
                },
                modifier = Modifier.shadow(10.dp),
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
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.colyak),
                    contentDescription = "",
                    modifier = Modifier.aspectRatio(1.5f)
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Lütfen E-maile gelen kodu girin")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (code.all { it.isEmpty() }) {
                            IconButton(
                                onClick = {
                                    val clipData: ClipData? = clipboardManager.primaryClip
                                    val pasteData = clipData?.getItemAt(0)?.text?.toString() ?: ""
                                    if (pasteData.length == 6) {
                                        pasteData.forEachIndexed { index, char ->
                                            code[index] = char.toString()
                                        }
                                        focusRequesters[5].requestFocus()
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Geçersiz kod. Kodunuz 6 karakter olmalı.")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.paste),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    code.forEachIndexed { index, value ->
                        SingleDigitInput(
                            value = value,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1) {
                                    if (newValue.isEmpty() && value.isNotEmpty() && index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                    }
                                    code[index] = newValue
                                    if (newValue.isNotEmpty() && index < 5) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            onBackspace = {
                                if (index > 0 && code[index].isEmpty()) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .focusOrder(focusRequesters[index]) {
                                    if (index < 5) {
                                        next = focusRequesters[index + 1]
                                    }
                                    if (index > 0) {
                                        previous = focusRequesters[index - 1]
                                    }
                                }
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    CustomizeButton(
                        onClick = {
                            scope.launch {
                                val response = verificationVM.verification(
                                    VerificationData(
                                        verificationId,
                                        code.joinToString("")
                                    ),
                                )
                                Log.e("verify", response.toString())
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
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    contentColor = Color.White
                ) {
                    val message = if (responseState.value) {
                        "Kod Doğrulama Başarılı. Giriş Ekranına Yönlendiriliyorsunuz Lütfen Bekleyin"
                    } else {
                        "Doğrulama Kodu Hatalı Lütfen Tekrar Deneyin"
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = message,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SingleDigitInput(
    value: String,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= 1) {
                if (newValue.isEmpty()) {
                    onBackspace()
                }
                onValueChange(newValue)
            }
        },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        modifier = modifier
            .padding(2.dp).clip(RoundedCornerShape(3)),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = colorResource(id = R.color.statusBarColor),
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = colorResource(id = R.color.statusBarColor),
        )
    )
}


