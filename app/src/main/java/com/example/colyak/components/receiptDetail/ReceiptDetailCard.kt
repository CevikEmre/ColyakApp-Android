package com.example.colyak.components.receiptDetail


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.colyak.components.functions.ImageFromUrl
import com.example.colyak.model.Receipt
import com.example.colyak.model.ReceiptItem

@Composable
fun ReceiptDetailCard(
    productList: List<ReceiptItem?>?,
    description: List<String?>?,
    modifier: Modifier,
    receipt: Receipt,
    navController: NavController
) {
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .aspectRatio(4/3f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ImageFromUrl(
                        url = "https://api.colyakdiyabet.com.tr/api/image/get/${receipt.imageId}",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            ReceiptDetailsTab(productList, description, receipt, navController)
        }
    }
}