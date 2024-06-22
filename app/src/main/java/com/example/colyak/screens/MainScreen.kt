package com.example.colyak.screens


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.model.MealDetail
import com.example.colyak.model.ReadyFoods
import com.example.colyak.model.Receipt
import com.example.colyak.ui.theme.ColyakTheme
import com.example.colyak.viewmodel.QuizViewModel
import com.example.colyak.viewmodel.ReadyFoodViewModel
import com.example.colyak.viewmodel.ReceiptViewModel
import kotlinx.coroutines.launch
import java.util.Locale

val mealList = listOf(
    MealDetail(R.drawable.lunch, "Öğün"),
)

var globalReceiptList: List<Receipt?>? = emptyList()
var globalReadyFoodList: List<ReadyFoods?>? = emptyList()

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale("tr")
        setContent {
            ColyakTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PageNavigations()
                }
            }
        }

    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}




@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController) {
    val receiptViewModel: ReceiptViewModel = viewModel()
    val readyFoodViewModel: ReadyFoodViewModel = viewModel()
    val receiptList by receiptViewModel.receiptList.collectAsState()
    val readyFoodList by readyFoodViewModel.readyFoodList.collectAsState()
    val quizViewModel: QuizViewModel = viewModel()
    val favoriteReceipts by receiptViewModel.favroiteReceiptList.collectAsState()
    Log.e("favoriteReceipts", favoriteReceipts.toString())

    LaunchedEffect(Unit) {
        receiptViewModel.getAll()
        readyFoodViewModel.getAllReadyFoods()
        quizViewModel.getAllQuiz()
        receiptViewModel.getFavorite5Receipts()
    }

    globalReceiptList = receiptList
    globalReadyFoodList = readyFoodList

    val scope = rememberCoroutineScope()
    val bottomNavList = listOf(
        "Ana Sayfa", "Tarifler"
    )
    val images =
        listOf(R.drawable.outline_home, R.drawable.chef, R.drawable.syringe)
    val pageState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        bottomNavList.count()
    }

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.route == Screens.MainScreen.screen) {
                    return
                }
                navController.popBackStack()
            }
        }
    }

    val dispatcher = (LocalContext.current as? ComponentActivity)?.onBackPressedDispatcher
    DisposableEffect(Unit) {
        dispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = colorResource(id = R.color.statusBarColor),
                content = {
                    for (page in bottomNavList.indices) {
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                unselectedIconColor = Color.DarkGray,
                                unselectedTextColor = Color.DarkGray,
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = colorResource(id = R.color.statusBarColor),
                            ),
                            selected = page == pageState.currentPage,
                            onClick = {
                                scope.launch {
                                    pageState.animateScrollToPage(page = page)
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = images[page]),
                                    contentDescription = ""
                                )
                            },
                            label = {
                                Text(
                                    text = bottomNavList[page]
                                )
                            }
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        HorizontalPager(
            state = pageState, modifier = Modifier.padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> MainContent(navController = navController)
                1 -> ReceiptScreen(navController = navController)
            }
        }
    }
}


