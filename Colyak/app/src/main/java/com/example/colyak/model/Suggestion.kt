package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Suggestion(
    @SerializedName("suggestionId")
    @Expose
    val suggestionId: Long,

    @SerializedName("suggestion")
    @Expose
    val suggestion: String,

    @SerializedName("userName")
    @Expose
    val userName: String,

    @SerializedName("bloodSugar")
    @Expose
    val createdDate: String,
)
