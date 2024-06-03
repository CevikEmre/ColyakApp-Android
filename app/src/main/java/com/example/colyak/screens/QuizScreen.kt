package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.cards.QuizCard
import com.example.colyak.viewmodel.quizList
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizScreen(navController: NavController) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = "Quiz")
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(id = R.color.appBarColor),
                titleContentColor = Color.White
            )
        )
    },
        content = { padding ->
            LazyColumn(
                content = {
                    items(quizList.size) {
                        val quiz = quizList[it]
                        QuizCard(
                            topic = quiz.topicName,
                            modifier = Modifier
                                .padding(6.dp)
                                .clickable {
                                    val quizJson = Gson().toJson(quiz)
                                    navController.navigate("${Screens.QuizDetailScreen.screen}/$quizJson")
                                }
                        )
                    }
                },
                modifier = Modifier.padding(padding)
            )
        })


}