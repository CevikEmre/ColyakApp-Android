package com.example.colyak.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.model.onBoardPages
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.launch

@Composable
fun UserGuideScreen(navController: NavController) {
    val pagerState = com.google.accompanist.pager.rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (pagerState.currentPage != onBoardPages.lastIndex) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }) {
                    Text(
                        text = "İleri",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.statusBarColor)
                    )
                }
            }
        }
        HorizontalPager(
            count = onBoardPages.size, state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            OnBoardScrenContent(onBoardPages[page])
        }
        HorizontalPagerIndicator(
            pagerState = pagerState, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = colorResource(id = R.color.statusBarColor)
        )
        AnimatedVisibility(visible = pagerState.currentPage == onBoardPages.lastIndex) {
            Button(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                onClick = {
                    navController.navigate(Screens.MainScreen.screen)
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colorResource(
                        id = R.color.statusBarColor
                    ), colorResource(id = R.color.white)
                )
            ) {
                Text(text = "Haydi Başlayalım")
            }
        }
    }
}