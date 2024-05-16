package com.example.colyak.screens


import SessionManager
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.colyak.R
import com.example.colyak.components.cards.MealCard
import com.example.colyak.components.cards.NutritionInfoCard
import com.example.colyak.model.Comment
import com.example.colyak.model.MealDetail
import com.example.colyak.model.PDFResponse
import com.example.colyak.model.Quiz
import com.example.colyak.model.ReadyFoods
import com.example.colyak.model.Receipt
import com.example.colyak.model.data.NavigationItem
import com.example.colyak.ui.theme.ColyakTheme
import com.example.colyak.viewmodel.LoginViewModel
import com.example.colyak.viewmodel.QuizViewModel
import com.example.colyak.viewmodel.ReadyFoodScreenViewModel
import com.example.colyak.viewmodel.ReceiptScreenViewModel
import com.example.colyak.viewmodel.answerList
import com.google.gson.Gson
import kotlinx.coroutines.launch

val mealList = listOf(
    MealDetail(R.drawable.lunch, "Öğün", 200, 650),
)

var globalReceiptList: List<Receipt?>? = emptyList()
var globalReadyFoodList: List<ReadyFoods?>? = emptyList()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}

@Composable
fun PageNavigations() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Login.screen,
    ) {
        composable(Screens.Login.screen) {
            LoginScreen(navController = navController)
        }
        composable(Screens.Register.screen) {
            RegisterScreen(navController = navController)
        }
        composable(Screens.MainScreen.screen) {
            MainScreen(navController = navController)
        }
        composable("${Screens.ReceiptDetailScreen.screen}/{receipt}") {
            navArgument("receipt") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("receipt")
            val receipt = Gson().fromJson(json, Receipt::class.java)
            ReceiptDetailScreen(receipt = receipt, navController = navController)
        }
        composable(Screens.Profile.screen) {
            ProfileScreen(navController)
        }
        composable("${Screens.MealDetail.screen}/{meal}") {
            navArgument("meal") {
                type = NavType.StringType
            }
            val mealJson = it.arguments?.getString("meal")
            val meal = Gson().fromJson(mealJson, MealDetail::class.java)
            MealDetailScreen(mealDetail = meal, navController = navController)
        }
        composable("${Screens.AddMealScreen.screen}/{mealName}") {
            val mealName = it.arguments?.getString("mealName") ?: ""
            AddMealScreen(
                mealName = mealName,
                navController = navController
            )
        }
        composable("${Screens.AddReceiptScreen.screen}/{receiptDetail}") {
            navArgument("receiptDetail") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("receiptDetail")
            val choose = Gson().fromJson(json, Receipt::class.java)
            AddReceiptScreen(receipt = choose, navController = navController)
        }
        composable("${Screens.AddReadyFoodScreen.screen}/{readyFoodDetail}") {
            navArgument("readyFoodDetail") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("readyFoodDetail")
            val choose = Gson().fromJson(json, ReadyFoods::class.java)
            AddReadyFoodScreen(readyFoods = choose, navController = navController)
        }
        composable(Screens.QuizScreen.screen) {
            QuizScreen(navController)
        }
        composable("${Screens.QuizDetailScreen.screen}/{quiz}") {
            navArgument("quiz") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("quiz")
            val quiz = Gson().fromJson(json, Quiz::class.java)
            QuizDetailScreen(quiz, navController)
        }
        composable(Screens.QuizReportScreen.screen) {
            QuizReportScreen(quizReportList = answerList, navController = navController)
        }
        composable("${Screens.CommentReplyScreen.screen}/{comment}") {
            navArgument("comment") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("comment")
            val comment = Gson().fromJson(json, Comment::class.java)
            CommentReplyScreen(comment = comment, navController = navController)
        }
        composable(Screens.VerificationScreen.screen) {
            VerificationScreen(navController)
        }
        composable(Screens.ForgotPasswordScreen.screen) {
            ForgotPasswordScreen(navController)
        }
        composable(Screens.SuggestionScreen.screen) {
            SuggestionScreen(navController)
        }
        composable(Screens.UsefulInformationScreen.screen) {
            UsefulInformationScreen(navController)
        }
        composable("${Screens.PdfDetailScreen.screen}/{pdf}") {
            navArgument("comment") {
                type = NavType.StringType
            }
            val json = it.arguments?.getString("pdf")
            val pdf = Gson().fromJson(json, PDFResponse::class.java)
            PdfDetailScreen(pdfResponse = pdf, navController = navController)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController) {
    val loginViewModel: LoginViewModel = viewModel()
    val receiptScreenViewModel: ReceiptScreenViewModel = viewModel()
    val readyFoodScreenViewModel: ReadyFoodScreenViewModel = viewModel()
    val loading by receiptScreenViewModel.loading.collectAsState()
    val receiptList by receiptScreenViewModel.receiptList.collectAsState()
    val readyFoodList by readyFoodScreenViewModel.readyFoodList.collectAsState()
    val quizViewModel: QuizViewModel = viewModel()

    LaunchedEffect(Unit) {
        receiptScreenViewModel.getAll()
        readyFoodScreenViewModel.getAllReadyFoods()
        quizViewModel.getAllQuiz()
    }

    globalReceiptList = receiptList
    globalReadyFoodList = readyFoodList

    val scope = rememberCoroutineScope()
    val bottomNavList = listOf(
        "Home", "Receipts", "Bolus"
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
                containerColor = colorResource(id = R.color.appBarColor),

                content = {
                    for (page in bottomNavList.indices) {
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                unselectedIconColor = Color.DarkGray,
                                unselectedTextColor = Color.DarkGray,
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = colorResource(id = R.color.appBarColor),
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
                2 -> BolusScreen()
            }
        }

    }
}

val navList = listOf(
    NavigationItem(
        title = "Profil",
        selectedIcon = R.drawable.account_circle,
        unselectedIcon = R.drawable.account_circle,
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
        selectedIcon = R.drawable.suggestion,
        unselectedIcon = R.drawable.suggestion,
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
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(-1) }
    var showAlert by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                navList.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            when (index) {
                                0 -> navController.navigate(Screens.Profile.screen)
                                1 -> navController.navigate(Screens.QuizScreen.screen)
                                2 -> navController.navigate(Screens.SuggestionScreen.screen)
                                3 -> navController.navigate(Screens.UsefulInformationScreen.screen)
                                4 -> showAlert = true
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
            content = { padding ->
                LazyColumn {
                    items(1) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(padding),
                            horizontalArrangement = Arrangement.Absolute.Center
                        ) {
                            Text(
                                text = "Hoşgeldin",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                    items(1) {
                        NutritionInfoCard(
                            padding,
                            caloriGoal = 2500,
                            carbGoal = 150,
                            carbTaken = 15,
                            fatGoal = 75,
                            fatTaken = 25,
                            caloriTaken = 1500,
                            proteinGoal = 125,
                            proteinTaken = 32,
                        )
                    }
                    items(1) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp, start = 18.dp, end = 18.dp)
                        ) {
                            Text(
                                text = "Beslenme",
                                fontWeight = FontWeight.Bold, fontSize = 20.sp
                            )
                            Text(
                                text = "Fazlası",
                                fontWeight = FontWeight.Bold, fontSize = 20.sp
                            )
                        }
                    }
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
                    sessionManager.clearSession()
                    navController.navigate(Screens.Login.screen)
                }
                )
            }
        )
    }
}

