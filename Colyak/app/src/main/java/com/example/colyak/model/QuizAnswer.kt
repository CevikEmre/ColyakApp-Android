package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuizAnswer(
    @SerializedName("questionId")
    @Expose
    val questionId: Long,

    @SerializedName("chosenAnswer")
    @Expose
    val chosenAnswer: String,

    @SerializedName("userName")
    @Expose
    val userName: String,

    @SerializedName("questionText")
    @Expose
    val questionText: String,

    @SerializedName("correctAnswer")
    @Expose
    val correctAnswer: String,

    @SerializedName("correct")
    @Expose
    val correct: Boolean,
)
