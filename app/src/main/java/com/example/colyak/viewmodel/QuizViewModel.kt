package com.example.colyak.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colyak.model.Quiz
import com.example.colyak.model.QuizAnswer
import com.example.colyak.model.data.AnswerData
import com.example.colyak.service.QuizService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

var quizList: List<Quiz> = emptyList()
var answerList = mutableStateListOf<QuizAnswer>()

class QuizViewModel : ViewModel() {
    private val _quizList = MutableStateFlow<List<Quiz>>(emptyList())
    private val _answerList = MutableStateFlow<List<QuizAnswer>>(emptyList())

    private val _answerResponse = MutableStateFlow<QuizAnswer?>(QuizAnswer(0, "", "", "", "", false))
    val answerResponse: StateFlow<QuizAnswer?> = _answerResponse

    suspend fun getAllQuiz(): List<Quiz> {
            try {
                val response = QuizService().getAllQuizs()
                _quizList.value = response ?: emptyList()
                quizList = _quizList.value

            } catch (e: Exception) {
                Log.e("QuizViewModel", "Fail", e)
            }
        return _quizList.value
    }

    suspend fun questionAnswer(answerData: AnswerData): QuizAnswer? {
            try {
                val response = QuizService().answer(answerData)
                if (response != null) {
                    _answerResponse.value = response
                }

            } catch (e: Exception) {
                Log.e("questionAnswer", "Fail", e)
            }
        return _answerResponse.value
    }

    suspend fun getQuizAnswer(quizId: Long): List<QuizAnswer> {
        viewModelScope.launch {
            try {
                val response = QuizService().getQuizAnswer(quizId)
                _answerList.value = response ?: emptyList()
                answerList = _answerList.value.toMutableStateList()
                answerList = response?.toMutableStateList()!!
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Fail", e)
            }
        }
        Log.e("AnswerList", _answerList.value.toString())
        return _answerList.value
    }
}