package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuestionList(
    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("question")
    @Expose
    val question: String,

    @SerializedName("choicesList")
    @Expose
    val choicesList: List<ChoicesList>,

    @SerializedName("correctAnswer")
    @Expose
    val correctAnswer: String,
)