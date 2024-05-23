package com.example.colyak.components.receiptDetail

import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.model.Comment
import com.example.colyak.model.Receipt
import com.example.colyak.model.ReceiptItem


@Composable
fun ReceiptDetailsTab(
    list:List<ReceiptItem?>?,
    discription:List<String?>?,
    receipt: Receipt,
    commentList:MutableList<Comment>,
    navController: NavController
) {
    val titles = listOf("Malzeme\n Listesi", "Tarif", "Besin\n Değerleri","Yorumlar")
    var tabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = tabIndex,
                contentColor = Color(0xFF333333),
                modifier = Modifier.background(Color.White),
                containerColor = Color(0xFFFFF1EC),
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
                            ) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
        },
        content = { paddingValues ->
            when (tabIndex) {
                0 -> ProductsTab(paddingValues,list)

                1 -> ReceiptTab(paddingValues,discription)

                2 -> NutritionValuesTab(paddingValues, receipt)

                3 -> CommentTab(paddingValues,receipt,navController)
            }

        }
    )
}