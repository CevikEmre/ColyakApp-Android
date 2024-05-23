package com.example.colyak.service

import android.util.Log
import com.example.colyak.model.Quiz
import com.example.colyak.model.QuizAnswer
import com.example.colyak.model.data.AnswerData
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyak.`interface`.QuizInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class QuizService {
    suspend fun getAllQuizs(): List<Quiz>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(QuizInterface::class.java)
                    .getAllQuiz()
                    .awaitResponse()
                if (response.isSuccessful) {
                    response.body()
                } else {

                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e("QuizService", "getAllQuizs request failed with code: $errorCode, message: $errorMessage")
                    null
                }

            } catch (e: Exception) {
                Log.e("QuizService", "Error sending getAllQuizs request", e)
                null
            }
        }
    }

    suspend fun answer(answerData: AnswerData): QuizAnswer? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(QuizInterface::class.java)
                    .answer(answerData)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e("QuizService", "answer request failed with code: $errorCode, message: $errorMessage")
                    null
                }
            } catch (e: Exception) {
                Log.e("QuizService", "Error sending answer request", e)
                null
            }
        }
    }

    suspend fun getQuizAnswer(quizId:Long): List<QuizAnswer>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(QuizInterface::class.java)
                    .getQuizAnswers(quizId)
                    .awaitResponse()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e("QuizService", "getQuizAnswer request failed with code: $errorCode, message: $errorMessage")
                    null
                }

            } catch (e: Exception) {
                Log.e("QuizService", "Error sending getQuizAnswer request", e)
                null
            }
        }
    }
}