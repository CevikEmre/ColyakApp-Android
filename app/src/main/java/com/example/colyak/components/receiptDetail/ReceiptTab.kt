@file:Suppress("NAME_SHADOWING")

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReceiptTab(
    paddingValues: PaddingValues,
    descriptionList: List<String?>?
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            if (descriptionList.isNullOrEmpty()){
                Column(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bu tarife herhangi bir tarif adımı eklenmemiştir.",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            LazyColumn(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                if (descriptionList != null) {
                    items(descriptionList.size) { index ->
                        val description = descriptionList[index]
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            if (description != null) {
                                Text(
                                    text = "${index.inc()} - " + description,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                            } else {
                                Text(text = "")
                            }
                        }
                        if (index < descriptionList.size - 1){
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                } else {
                    items(1) {
                        Text(text = "Tarif Detayları Bulunamadı")
                    }
                }
            }
        }
    }
}