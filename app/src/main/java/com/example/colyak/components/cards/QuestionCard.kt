package com.example.colyak.components.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colyak.R
import com.example.colyak.model.QuestionList
import com.example.colyak.model.data.AnswerData
import com.example.colyak.viewmodel.QuizViewModel
import kotlinx.coroutines.delay
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
    val answer by quizViewModel.answerResponse.collectAsState()
    var showAlert by remember { mutableStateOf(false) }


    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier.padding(all = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = questionList.question)
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            questionList.choicesList.forEachIndexed { index, choice ->
                val isSelected = selectedIndex.intValue == index
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = {
                            selectedIndex.intValue = index
                            if (selectedIndex.intValue != -1) {
                                scope.launch {
                                    quizViewModel.questionAnswer(
                                        AnswerData(
                                            questionId = questionList.id,
                                            chosenAnswer = choice.choice
                                        )
                                    )
                                    onAnswered()
                                    delay(800)
                                    showAlert = true
                                }

                            }

                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(id = R.color.appBarColor),
                            unselectedColor = Color.LightGray
                        )
                    )
                    Text(text = choice.choice)
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
}

