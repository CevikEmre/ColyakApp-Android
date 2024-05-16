package com.example.colyak.service.interfaces

import com.example.colyak.model.Quiz
import com.example.colyak.model.QuizAnswer
import com.example.colyak.model.data.AnswerData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QuizInterface {
    @GET("/api/quiz/all")
    fun getAllQuiz(): Call<List<Quiz>>

    @POST("/api/user-answer/submit-answer")
    suspend fun answer(@Body answerData: AnswerData): Response<QuizAnswer?>

    @GET("/api/user-answer/user-answers/{quizId}")
    fun getQuizAnswers(@Path("quizId") quizId:Long): Call<List<QuizAnswer>>
}