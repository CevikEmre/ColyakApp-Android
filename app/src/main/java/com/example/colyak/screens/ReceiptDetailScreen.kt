package com.example.colyak.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.receiptDetail.ReceiptDetailCard
import com.example.colyak.model.Receipt
import com.example.colyak.model.data.FavoriteData
import com.example.colyak.viewmodel.CommentViewModel
import com.example.colyak.viewmodel.FavoriteViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun ReceiptDetailScreen(receipt: Receipt, navController: NavController) {
    val commentVM: CommentViewModel = viewModel()
    val favoriteVM: FavoriteViewModel = viewModel()
    val scope = rememberCoroutineScope()
    var isReceiptFavorite by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        receipt.id?.let { commentVM.getCommentsById(it) }
        val favoritedReceipts = favoriteVM.getAllFavoriteReceipts()
        if (favoritedReceipts != null) {
            isReceiptFavorite = favoritedReceipts.any { it!!.id == receipt.id }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    receipt.receiptName?.let {
                        Text(
                            text = it,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.Black
                ),
                modifier = Modifier.shadow(10.dp),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (isReceiptFavorite) {
                                    val favoriteData = receipt.id?.let { FavoriteData(it) }
                                    favoriteVM.unlikeReceipt(favoriteData)
                                    isReceiptFavorite = false
                                    Toast.makeText(
                                        ColyakApp.applicationContext(),
                                        "Favorilerden çıkarıldı",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val favoriteData = receipt.id?.let { FavoriteData(it) }
                                    favoriteData?.let { favoriteVM.likeReceipt(it) }
                                    isReceiptFavorite = true
                                    Toast.makeText(
                                        ColyakApp.applicationContext(),
                                        "Favorilere eklendi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    ) {
                        val scale by animateFloatAsState(
                            targetValue = if (isReceiptFavorite) 1.05f else 1f,
                            animationSpec = tween(durationMillis = 250), label = ""
                        )
                        Icon(
                            painter = painterResource(id = if (isReceiptFavorite) R.drawable.favorite else R.drawable.favorite_filled),
                            contentDescription = "",
                            tint = if (isReceiptFavorite) Color.Red else Color.Black,
                            modifier = Modifier
                                .scale(scale)
                                .graphicsLayer(scaleX = scale, scaleY = scale)
                        )
                    }

                },
            )
        },
        content = { padding ->
            ReceiptDetailCard(
                productList = receipt.receiptItems,
                description = receipt.receiptDetails,
                modifier = Modifier.padding(padding),
                receipt,
                navController
            )
        }
    )
}