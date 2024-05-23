package com.example.colyak.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionData(
    @SerializedName("suggestion")
    @Expose
    val suggestion: String
)
