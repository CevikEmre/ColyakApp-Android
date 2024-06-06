package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.cards.AddMealCard
import com.example.colyak.components.functions.SearchTextField
import com.example.colyak.model.ReadyFoods
import com.example.colyak.model.Receipt
import com.example.colyak.viewmodel.ReceiptViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


var typeList = listOf("Tarif", "Hazır Yemek")
var visible = mutableStateOf(false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(mealName: String, navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "$mealName Ekle")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                actions = {}
            )
        },
        content = { padding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(padding)
                    .padding(3.dp)
                    .fillMaxSize()
            ) {
                AddMealToList()
            }
        }
    )
    if (visible.value) {
        AnimatedPopup()
    }
}

var isVisibleReceipt = mutableStateOf(false)
var isVisibleReadyFood = mutableStateOf(false)


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddMealToList() {
    val receiptScreenViewModel: ReceiptViewModel = viewModel()
    val receiptList by receiptScreenViewModel.filteredReceiptList.collectAsState()
    val selectedTypeIndex = remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedReceipt by remember { mutableStateOf<Receipt?>(null) }
    var selectedReadyFood by remember { mutableStateOf<ReadyFoods?>(null) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Card(Modifier.padding(5.dp)) {
            SearchTextField { query ->
                searchQuery = query
            }
        }
        TabRow(
            selectedTabIndex = selectedTypeIndex.intValue,
            contentColor = colorResource(id = R.color.appBarColor),
            modifier = Modifier.background(Color.White),
            containerColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTypeIndex.intValue]),
                    color = colorResource(id = R.color.appBarColor)
                )
            }
        ) {
            typeList.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            title,
                            fontSize = 15.sp
                        )
                    },
                    selected = selectedTypeIndex.intValue == index,
                    onClick = {
                        selectedTypeIndex.intValue = index
                    }
                )
            }
        }

        when (selectedTypeIndex.intValue) {
            0 -> {
                val filteredReceiptList =
                    receiptList?.filter {
                        it?.receiptName!!.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (filteredReceiptList != null) {
                        items(filteredReceiptList.size) {
                            val receipt = filteredReceiptList[it]
                            AddMealCard(cardName = receipt?.receiptName, onClick = {
                                scope.launch {
                                    selectedReceipt = receipt
                                    isVisibleReceipt.value = true
                                }
                            })
                        }
                    }
                }
            }

            1 -> {
                val filteredReadyFoodList = globalReadyFoodList?.filter {
                    it?.name?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true
                }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (filteredReadyFoodList != null) {
                        items(filteredReadyFoodList.size) {
                            val readyFood = filteredReadyFoodList[it]
                            AddMealCard(cardName = readyFood?.name, onClick = {
                                scope.launch {
                                    selectedReadyFood = readyFood
                                    isVisibleReadyFood.value = true
                                }
                            })
                        }
                    }
                }
            }
        }
        if (isVisibleReceipt.value) {
            ModalBottomSheet(
                modifier = Modifier.height(IntrinsicSize.Min),
                sheetState = sheetState,
                onDismissRequest = {
                    isVisibleReceipt.value = false
                },

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    selectedReceipt?.let { receipt ->
                        AddReceiptScreen(receipt = receipt)
                    }
                }

            }
        }
        if (isVisibleReadyFood.value) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    isVisibleReadyFood.value = false
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    selectedReadyFood?.let { readyFood ->
                        AddReadyFoodScreen(readyFoods = readyFood)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedPopup() {
    LaunchedEffect(Unit) {
        delay(1500)
        visible.value = false
    }

    AnimatedVisibility(
        visible = visible.value,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        ) + fadeIn(initialAlpha = 0.5f),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        ) + fadeOut(),
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        Card(
            modifier = Modifier
                .padding(top = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF1EC)
            ),
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Öğüne Eklenenler",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                ) {
                    items(eatenMealList.size)
                    {
                        Row(
                            Modifier
                                .padding(horizontal = 5.dp, vertical = 5.dp),
                            horizontalArrangement = Arrangement.End
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(5.dp)
                            ) {
                                Column {
                                    eatenMealList[it].mealName?.let { it1 ->
                                        Text(
                                            text = it1,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W600,
                                            maxLines = 2,
                                            overflow = TextOverflow.Visible,
                                            modifier = Modifier.padding(bottom = 5.dp)
                                        )
                                    }
                                    Text(
                                        text = "Karbonhidrat : ${eatenMealList[it].carb} Gram",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

