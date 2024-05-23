package com.example.colyak.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnswerData(

    @SerializedName("questionId")
    @Expose
    val questionId: Long,

    @SerializedName("chosenAnswer")
    @Expose
    val chosenAnswer: String,
)

