package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.cards.QuestionCard
import com.example.colyak.model.Quiz
import com.example.colyak.viewmodel.QuizViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizDetailScreen(quiz: Quiz, navController: NavController) {
    val scope = rememberCoroutineScope()
    val quizViewModel: QuizViewModel = viewModel()
    var currentQuestionIndex by remember { mutableIntStateOf(0) }

    val onNextQuestionClicked: () -> Unit = {
        currentQuestionIndex++
        if (currentQuestionIndex == quiz.questionList.size) {
            navController.navigate(Screens.QuizReportScreen.screen)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = quiz.topicName)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) {
                    Text(
                        text = "${currentQuestionIndex + 1} / ${quiz.questionList.size}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                items(quiz.questionList.size) { index ->
                    if (index == currentQuestionIndex) {
                        QuestionCard(
                            questionList = quiz.questionList[index],
                            onAnswered = {
                                scope.launch {
                                    quizViewModel.getQuizAnswer(quizId = quiz.id)
                                }
                            },
                            onNextQuestionClicked = onNextQuestionClicked
                        )
                    }
                }
            }
        }
    )
}