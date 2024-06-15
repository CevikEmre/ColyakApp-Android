package com.example.colyak.screens

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colyak.R
import com.example.colyak.components.consts.CustomizeButton
import com.example.colyak.model.QuizAnswer

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuizReportScreen(quizReportList: List<QuizAnswer>, navController: NavController) {
    val (correctAnswersCount, incorrectAnswersCount) = countCorrectAndIncorrectAnswers(quizReportList)
    val score = calculateScore(correctAnswersCount, quizReportList.size)
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.route == Screens.QuizReportScreen.screen) {
                    return
                }
                navController.popBackStack()
            }
        }
    }

    val dispatcher = (LocalContext.current as? ComponentActivity)?.onBackPressedDispatcher
    DisposableEffect(Unit) {
        dispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Sonuç Sayfası")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.appBarColor),
                    titleContentColor = Color.Black
                ),
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(16.dp)
                            ) {
                                Canvas(modifier = Modifier.size(200.dp)) {
                                    drawArc(
                                        color = Color.Red,
                                        startAngle = -90f,
                                        sweepAngle = 360f,
                                        useCenter = false,
                                        style = Stroke(12f, cap = StrokeCap.Butt)
                                    )
                                }
                                Canvas(modifier = Modifier.size(200.dp)) {
                                    val sweepAngle =
                                        360 * correctAnswersCount.toFloat() / quizReportList.size
                                    drawArc(
                                        color = Color.Green,
                                        startAngle = -90f,
                                        sweepAngle = sweepAngle,
                                        useCenter = false,
                                        style = Stroke(12f, cap = StrokeCap.Round)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$score / 100",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )

                                }
                            }
                            Row {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "",
                                    tint = Color.Green
                                )
                                Text(
                                    text = "Doğru Cevap Sayısı : $correctAnswersCount",
                                    modifier = Modifier.padding(start = 6.dp)
                                )
                            }
                            Row {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "",
                                    tint = Color.Red
                                )
                                Text(
                                    text = "Yanlış Cevap Sayısı : $incorrectAnswersCount",
                                    modifier = Modifier.padding(start = 6.dp)
                                )
                            }
                        }

                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()

                ) {

                    items(quizReportList.size) {
                        val quizReport = quizReportList[it]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(text = quizReport.questionText + " ? ")
                                    Text(text = "Verdiğin Cevap : ${quizReport.chosenAnswer}")
                                    if (quizReport.correct) {
                                        Row {
                                            Text(text = "DOĞRU", color = Color.Green)
                                        }
                                    } else {
                                        Column {
                                            Text(text = "Yanlış", color = Color.Red)
                                            Text(text = "Doğru Cevap : " + quizReport.correctAnswer)
                                        }
                                    }
                                }

                            }
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Absolute.Center
                        ) {
                            CustomizeButton(
                                onClick = {
                                    navController.navigate(Screens.MainScreen.screen)
                                },
                                buttonText = "Ana Sayfa",
                                backgroundColor = colorResource(
                                    id = R.color.statusBarColor
                                )
                            )
                            Spacer(modifier = Modifier.height(25.dp))
                        }
                    }
                }
            }
        }
    )
}
fun countCorrectAndIncorrectAnswers(quizReportList: List<QuizAnswer>): Pair<Int, Int> {
    val correctAnswersCount = quizReportList.count { it.correct }
    val incorrectAnswersCount = quizReportList.size - correctAnswersCount
    return Pair(correctAnswersCount, incorrectAnswersCount)
}
fun calculateScore(correctAnswersCount: Int, totalQuestions: Int): Int {
    return if (totalQuestions > 0) {
        (correctAnswersCount * 100) / totalQuestions
    } else {
        0
    }
}
