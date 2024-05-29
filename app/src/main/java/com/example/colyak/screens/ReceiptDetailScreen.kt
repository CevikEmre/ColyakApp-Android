package com.example.colyak.screens

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    val commentList by commentVM.commentList.collectAsState()
    val scope = rememberCoroutineScope()
    var isReceiptFavorite by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        commentVM.getCommentsById(receipt.id)
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
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                                if (isReceiptFavorite) {
                                    scope.launch {
                                        val favoriteData = FavoriteData(receipt.id)
                                        favoriteVM.unlikeReceipt(favoriteData)
                                        isReceiptFavorite = false
                                        Toast.makeText(context, "Favorilerden çıkarıldı", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    scope.launch {
                                        val favoriteData = FavoriteData(receipt.id)
                                        favoriteVM.likeReceipt(favoriteData)
                                        isReceiptFavorite = true
                                        Toast.makeText(context, "Favorilere eklendi", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = if (isReceiptFavorite) R.drawable.favorite else R.drawable.favorite_filled),
                            contentDescription = "",
                            tint = if (isReceiptFavorite) Color.Red else Color.White
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