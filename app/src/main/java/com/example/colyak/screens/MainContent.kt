package com.example.colyak.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
        title = "Bolus Hesapla",
        selectedIcon = R.drawable.syringe,
        unselectedIcon = R.drawable.syringe,
    ),
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
                                0 -> navController.navigate(Screens.BolusScreen.screen)
                                1 -> navController.navigate(Screens.QuizScreen.screen)
                                2 -> navController.navigate(Screens.SuggestionScreen.screen)
                                3 -> navController.navigate(Screens.UsefulInformationScreen.screen)
                                4 -> navController.navigate(Screens.BarcodeScreen.screen)
                                5 -> navController.navigate(Screens.DateRangePickerScreen.screen)
                                6 -> showAlert = true
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
                        titleContentColor = Color.Black
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                } else {
                                    drawerState.open()
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.menu),
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screens.UserGuideScreen.screen) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.question_mark),
                                contentDescription = "",
                                tint = Color.Black
                            )

                        }
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                    Snackbar(snackbarData = snackbarData)
                }
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 20.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(150.dp)) {
                                drawArc(
                                    color = Color.LightGray,
                                    startAngle = -90f,
                                    sweepAngle = 360 * 1f,
                                    useCenter = false,
                                    style = Stroke(5f, cap = StrokeCap.Round)
                                )
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.person),
                                contentDescription = "",
                                tint = Color(0xFF333333),
                                modifier = Modifier

                            )
                        }
                        Text(
                            text = "Hoşgeldin , " + loginResponse.userName + "!",
                            fontSize = 20.sp,
                            color = Color(0xFF4A4A4A),
                            modifier = Modifier.padding(start = 12.dp)
                        )
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
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
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
                                                        val formattedReceiptJson =
                                                            Uri.encode(receiptJson)
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
}
