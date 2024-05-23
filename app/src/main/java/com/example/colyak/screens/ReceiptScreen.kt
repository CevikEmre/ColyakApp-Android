package com.example.colyak.screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.CircularIndeterminateProgressBar
import com.example.colyak.components.cards.ReceiptCard
import com.example.colyak.viewmodel.FavoriteViewModel
import com.example.colyak.viewmodel.ReceiptViewModel
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

val titles = listOf("Tarifler", "Favori Tarifler")

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ImageFromUrl(url: String, modifier: Modifier) {
    val imageBitmap = remember(url) { mutableStateOf<ImageBitmap?>(null) }

    remember(url) {
        GlobalScope.launch(Dispatchers.Main) {
            val bitmap = getBitmapFromUrl(url)
            imageBitmap.value = bitmap
        }
    }

    imageBitmap.value?.let { bitmap ->
        Image(
            bitmap = bitmap,
            contentDescription = null,
            modifier,
            contentScale = ContentScale.FillBounds
        )
    }
}

private suspend fun getBitmapFromUrl(urlString: String): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@Composable
fun ReceiptScreen(navController: NavController) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val favoriteVM: FavoriteViewModel = viewModel()
    val favoriteList by favoriteVM.favoriteReceiptList.observeAsState()
    val receiptViewModel: ReceiptViewModel = viewModel()
    val loading by receiptViewModel.loading.collectAsState()
    val receiptList by receiptViewModel.receiptList.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        receiptViewModel.getAll()
        favoriteVM.getAllFavoriteReceipts()
    }

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = tabIndex,
                contentColor = colorResource(id = R.color.appBarColor),
                modifier = Modifier.background(Color.White),
                containerColor = Color.White,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                        color = colorResource(id = R.color.appBarColor)
                    )
                }
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                title,
                                fontSize = 15.sp
                            )
                        },
                        selected = tabIndex == index,
                        onClick = {
                            tabIndex = index
                        }
                    )
                }
            }
        },
        content = { paddingValues ->
            if (loading) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularIndeterminateProgressBar(isDisplay = loading)
                }
            } else {
                when (tabIndex) {
                   0 -> LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        content = {
                            if (receiptList?.isEmpty() == false) {
                                items(receiptList!!.size) {
                                    val receipt = receiptList?.get(it)
                                    if (receipt != null) {
                                        val trimmedName = if (receipt.receiptName?.length!! > 26) {
                                            receipt.receiptName.substring(0,26) + "..."
                                        } else {
                                            receipt.receiptName
                                        }
                                        ReceiptCard(
                                            cardName = trimmedName,
                                            carb = if (receipt.nutritionalValuesList?.isEmpty() == true) "0" else "Karb : " + "${
                                                receipt.nutritionalValuesList?.get(0)?.carbohydrateAmount
                                            }",
                                            image = {
                                                ImageFromUrl(
                                                    url = "https://api.colyakdiyabet.com.tr/api/image/get/${receipt.imageId}",
                                                    modifier = Modifier.aspectRatio(ratio = 1.28f)
                                                )
                                            },
                                            modifier = Modifier
                                                .clickable {
                                                    scope.launch {
                                                        try {
                                                            val receiptJson = Gson().toJson(receipt)
                                                            navController.navigate("${Screens.ReceiptDetailScreen.screen}/$receiptJson")
                                                        } catch (e: Exception) {
                                                            Log.e("NavigationError", "Tarif detayına yönlendirilirken bir hata oluştu", e)
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
                            }
                        },
                        modifier = Modifier.padding(paddingValues)
                    )


                        1 -> LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        content = {
                            favoriteList?.let { favorite ->
                                items(favorite.size) {
                                    val receipt = favorite[it]
                                    if (receipt != null) {
                                        val trimmedName = if (receipt.receiptName?.length!! > 26) {
                                            receipt.receiptName.substring(0,26) + "..."
                                        } else {
                                            receipt.receiptName
                                        }
                                        ReceiptCard(
                                            cardName = trimmedName,
                                            carb = "Karb : " + "${
                                                receipt.nutritionalValuesList?.get(0)?.carbohydrateAmount
                                            }",
                                            image = {
                                                ImageFromUrl(
                                                    url = "https://api.colyakdiyabet.com.tr/api/image/get/${receipt.imageId}",
                                                    modifier = Modifier.aspectRatio(ratio = 1.28f)
                                                )
                                            },
                                            modifier = Modifier
                                                .clickable {
                                                    scope.launch {
                                                        try {
                                                            val receiptJson = Gson().toJson(receipt)
                                                            navController.navigate("receiptDetailScreen/$receiptJson")
                                                        } catch (e: Exception) {
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
                            }
                        }, modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(snackbarData = snackbarData)
            }
        }
    )
}
