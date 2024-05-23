package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Quiz(
    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("topicName")
    @Expose
    val topicName: String,

    @SerializedName("questionList")
    @Expose
    val questionList: List<QuestionList>,

    @SerializedName("deleted")
    @Expose
    val deleted: Boolean,
)
