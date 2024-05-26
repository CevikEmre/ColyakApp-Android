package com.example.colyak.components.receiptDetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReceiptDetailsTab(
    list: List<ReceiptItem?>?,
    discription: List<String?>?,
    receipt: Receipt,
    commentList: MutableList<Comment>,
    navController: NavController
) {
    val titles = listOf("Malzeme\n Listesi", "Tarif", "Besin\n Değerleri", "Yorumlar")
    var tabIndex by remember { mutableIntStateOf(0) }
    val pageState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        4
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(tabIndex) {
        pageState.scrollToPage(tabIndex)
    }

    LaunchedEffect(pageState.currentPage) {
        tabIndex = pageState.currentPage
    }

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
                            )
                        },
                        selected = tabIndex == index,
                        onClick = {
                            coroutineScope.launch {
                                tabIndex = index
                                pageState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            HorizontalPager(
                state = pageState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> ProductsTab(innerPadding, list)
                    1 -> ReceiptTab(innerPadding, discription)
                    2 -> NutritionValuesTab(innerPadding, receipt)
                    3 -> CommentTab(innerPadding, receipt, navController)
                }
            }
        }
    )
}
