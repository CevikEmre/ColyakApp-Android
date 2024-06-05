package com.example.colyak.components.cards

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colyak.R
import com.example.colyak.components.functions.ImageFromUrl
import com.example.colyak.model.QuestionList
import com.example.colyak.model.QuizAnswer
import com.example.colyak.model.data.AnswerData
import com.example.colyak.viewmodel.QuizViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun QuestionCard(
    questionList: QuestionList,
    onAnswered: () -> Unit,
    onNextQuestionClicked: () -> Unit
) {
    val selectedIndex = remember { mutableIntStateOf(-1) }
    val quizViewModel: QuizViewModel = viewModel()
    val scope = rememberCoroutineScope()
    var answer by remember { mutableStateOf<QuizAnswer?>(null) }
    answer = quizViewModel.answerResponse.collectAsState().value

    var showAlert by remember { mutableStateOf(false) }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 2.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(questionList.question, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            if (questionList.choicesList.any { it.imageId != null && it.imageId.toInt() != 0 }) {
                LazyVerticalGrid(
                    modifier = Modifier.heightIn(max = 700.dp),
                    columns = GridCells.Fixed(2),
                    content = {
                        items(questionList.choicesList.size) {
                            val choice = questionList.choicesList[it]
                            QuestionCardWithImage(
                                image = {
                                    ImageFromUrl(
                                        url = "https://api.colyakdiyabet.com.tr/api/image/get/${choice.imageId}",
                                        modifier = Modifier.aspectRatio(ratio = 1.28f)
                                    )
                                },
                                onCardClick = {
                                    if (selectedIndex.intValue == -1) {
                                        selectedIndex.intValue = it
                                        scope.launch {
                                            quizViewModel.questionAnswer(
                                                AnswerData(
                                                    questionId = questionList.id,
                                                    chosenAnswer = choice.choice
                                                ),
                                            )
                                            onAnswered()
                                            showAlert = true
                                        }
                                    }
                                },
                                answer = choice.choice
                            )
                        }
                    }
                )
            } else {
                questionList.choicesList.forEachIndexed { index, choice ->
                    val isSelected = selectedIndex.intValue == index
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable {
                            if (selectedIndex.intValue == -1) {
                                selectedIndex.intValue = index
                                scope.launch {
                                    try {
                                        val response = quizViewModel.questionAnswer(
                                            AnswerData(
                                                questionId = questionList.id,
                                                chosenAnswer = choice.choice
                                            ),
                                        )
                                        if (response != null) {
                                            answer = response
                                            onAnswered()
                                            showAlert = true
                                        }
                                    } catch (e: Exception) {
                                        Log.e("QuestionCard", "Fail", e)
                                    }
                                }

                            }
                        },
                    ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                if (selectedIndex.intValue == -1) {
                                    selectedIndex.intValue = index
                                    scope.launch {
                                        try {
                                            val response = quizViewModel.questionAnswer(
                                                AnswerData(
                                                    questionId = questionList.id,
                                                    chosenAnswer = choice.choice
                                                ),
                                            )
                                            if (response != null) {
                                                answer = response
                                                onAnswered()
                                                showAlert = true
                                            }
                                        } catch (e: Exception) {
                                            Log.e("QuestionCard", "Fail", e)
                                        }
                                    }

                                }
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colorResource(id = R.color.appBarColor),
                                unselectedColor = Color.LightGray
                            )
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = choice.choice)
                        }
                    }
                }
            }
        }
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = {
                if (answer?.correctAnswer == answer?.chosenAnswer) {
                    Text(text = "Doğru Cevap", color = Color.Green)
                } else {
                    Text(text = "Yanlış Cevap", color = Color.Red)
                }
            },
            text = {
                if (answer?.correctAnswer == answer?.chosenAnswer) {
                    Text(text = "Tebrikler Verdiğiniz Cevap Doğru ☻", fontSize = 16.sp)
                } else {
                    Text(
                        text = "Yanlış Cevap\nDoğru Cevap: ${answer?.correctAnswer}",
                        fontSize = 16.sp
                    )
                }
            },
            confirmButton = {
                Text(
                    text = "Tamam",
                    modifier = Modifier.clickable {
                        showAlert = false
                        onNextQuestionClicked()
                    }
                )
            },
            containerColor = Color.White
        )
    }
}
