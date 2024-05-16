package com.example.colyak.components.receiptDetail


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.model.Comment
import com.example.colyak.model.Receipt
import com.example.colyak.model.ReceiptItem
import com.example.colyak.screens.ImageFromUrl
import com.example.colyak.viewmodel.ReplyViewModel

@Composable
fun ReceiptDetailCard(
    productList: List<ReceiptItem?>?,
    description: List<String?>?,
    modifier: Modifier,
    receipt: Receipt,
    commentList:MutableList<Comment>,
    navController: NavController
) {
    val replyVM:ReplyViewModel = viewModel()
    LaunchedEffect(Unit) {
        replyVM.getCommentsRepliesByReceiptId(receipt.id)
    }
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(250.dp)
                    .border(3.dp, colorResource(id = R.color.appBarColor), CircleShape)
                    .clip(CircleShape)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ImageFromUrl(
                        url = "https://api.colyakdiyabet.com.tr/api/image/get/${receipt.imageId}",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            ReceiptDetailsTab(productList, description, receipt,commentList,navController)
        }
    }
}