package com.example.colyak.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.colyak.components.cards.MealCard
import com.example.colyak.components.cards.ReceiptCard
import com.example.colyak.components.functions.ImageFromUrl
import com.example.colyak.model.data.NavigationItem
import com.example.colyak.session.SessionManager
import com.example.colyak.viewmodel.LoginViewModel
import com.example.colyak.viewmodel.ReceiptViewModel
import com.example.colyak.viewmodel.loginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch


val navList = listOf(
    NavigationItem(
        title = "Quiz",
        selectedIcon = R.drawable.quiz,
        unselectedIcon = R.drawable.quiz,
    ),
    NavigationItem(
        title = "Öneriler",
        selectedIcon = R.drawable.suggestion,
        unselectedIcon = R.drawable.suggestion,
    ),
    NavigationItem(
        title = "Faydalı Bilgiler",
        selectedIcon = R.drawable.menu_book,
        unselectedIcon = R.drawable.menu_book,
    ),
    NavigationItem(
        title = "Barkod Okut",
        selectedIcon = R.drawable.barcode,
        unselectedIcon = R.drawable.barcode,
    ),
    NavigationItem(
        title = "Raporlarım",
        selectedIcon = R.drawable.meal_report,
        unselectedIcon = R.drawable.meal_report,
    ),
    NavigationItem(
        title = "Çıkış Yap",
        selectedIcon = R.drawable.logout,
        unselectedIcon = R.drawable.logout,
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent(navController: NavController) {
    val isExpanded = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex by rememberSaveable { mutableIntStateOf(-1) }
    var showAlert by remember { mutableStateOf(false) }
    val showTokenAlert by remember { mutableStateOf(loginResponse.userName.isEmpty()) }
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val loginVM: LoginViewModel = viewModel()
    val receiptViewModel: ReceiptViewModel = viewModel()
    val favoriteReceipts by receiptViewModel.favroiteReceiptList.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val loading by receiptViewModel.favoriteLoading.collectAsState()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Image(painter = painterResource(id = R.drawable.colyak), contentDescription = "")
                navList.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Screens.QuizScreen.screen)
                                1 -> navController.navigate(Screens.SuggestionScreen.screen)
                                2 -> navController.navigate(Screens.UsefulInformationScreen.screen)
                                3 -> navController.navigate(Screens.BarcodeScreen.screen)
                                4 -> navController.navigate(Screens.DateRangePickerScreen.screen)
                                5 -> showAlert = true
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    }
                                ), contentDescription = ""
                            )
                        },
                        badge = {
                            item.badgeCount?.let {
                                Text(text = item.badgeCount.toString())
                            }
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        scrimColor = Color.Black.copy(alpha = 0.32f),
    )
    {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "ColyakApp"
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(id = R.color.appBarColor),
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    isExpanded.value = !isExpanded.value
                                    if (isExpanded.value) {
                                        open()
                                    } else close()
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.menu),
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp)),
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                    Snackbar(snackbarData = snackbarData)
                }
            },
            content = { padding ->
                Column(
                    modifier = Modifier.fillMaxWidth().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.person),
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "Hoşgeldin , " + loginResponse.userName ,
                            fontSize = 20.sp,
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "En çok tercih edilen 5 tarifimiz ☺",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W400,
                                color = Color.Black
                            )
                        }
                    }
                    if (loading) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularIndeterminateProgressBar(isDisplay = loading)
                        }
                    } else {
                        LazyRow(
                            content = {
                                items(favoriteReceipts?.size ?: 0) {
                                    val receipt = favoriteReceipts?.get(it)
                                    ReceiptCard(
                                        cardName = receipt?.receiptName,
                                        image = {
                                            ImageFromUrl(
                                                url = "https://api.colyakdiyabet.com.tr/api/image/get/${receipt?.imageId}",
                                                modifier = Modifier.aspectRatio(ratio = 1.28f)
                                            )
                                        },
                                        modifier = Modifier
                                            .clickable {
                                                scope.launch {
                                                    try {
                                                        val receiptJson = Gson().toJson(receipt)
                                                        val formattedReceiptJson = Uri.encode(receiptJson)
                                                        navController.navigate("${Screens.ReceiptDetailScreen.screen}/$formattedReceiptJson")
                                                    } catch (e: Exception) {
                                                        Log.e(
                                                            "NavigationError",
                                                            "Tarif detayına yönlendirilirken bir hata oluştu",
                                                            e
                                                        )
                                                        snackbarHostState.showSnackbar(
                                                            "Tarif detayına yönlendirilirken bir hata oluştu."
                                                        )
                                                    }
                                                }

                                            }
                                            .padding(all = 5.dp)
                                    )
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyColumn {
                        items(mealList.count()) {
                            val meal = mealList[it]
                            val mealJson = Gson().toJson(meal)
                            MealCard(
                                meal,
                                onClick = { navController.navigate("${Screens.MealDetail.screen}/$mealJson") }
                            )
                        }
                    }
                }
            }
        )
    }
    if (showAlert) {
        AlertDialog(
            onDismissRequest = {
                showAlert = false
            },
            title = { Text("Çıkış Yap") },
            text = { Text("Çıkış yapmak istediğinize emin misiniz ? ") },
            confirmButton = {
                Text(text = "Evet", modifier = Modifier.clickable {
                    showAlert = false
                    loginVM.clearLoginResponse()
                    sessionManager.clearSession()
                    navController.navigate(Screens.Login.screen)
                }
                )
            },
            dismissButton = {
                Text(text = "Hayır", modifier = Modifier.clickable {
                    showAlert = false
                }
                )
            },
        )
    }
    if (showTokenAlert) {
        AlertDialog(
            onDismissRequest = {
                showAlert = false
            },
            title = { Text("Oturum Süresi") },
            text = { Text("Oturum süreniz doldu lütfen tekrar giriş yapınız") },
            confirmButton = {
                Text(text = "Tamam", modifier = Modifier.clickable {
                    showAlert = false
                    navController.navigate(Screens.Login.screen)
                }
                )
            }
        )
    }
}