package com.example.colyak.components.receiptDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colyak.model.ReceiptItem

@Composable
fun ProductsTab(
    paddingValues: PaddingValues,
    list: List<ReceiptItem?>?
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = CardDefaults.cardElevation(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            LazyColumn(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                if (list != null) {
                    items(list.size) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${list[it]?.productName}", fontSize = 16.sp)
                            Text(
                                text = "${list?.get(it)?.unit?.toInt()} ${list?.get(it)?.type}",
                                Modifier.padding(
                                    top = 6.dp,
                                    bottom = 6.dp,
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                            )
                        }

                    }
                } else {
                    items(1) {
                        Text(text = "Malzeleme Bulunamadı")
                    }
                }
            }
        }
    }
}