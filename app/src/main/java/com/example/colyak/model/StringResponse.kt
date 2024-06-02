package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StringResponse(
    @SerializedName("code")
    @Expose
    val code: Int,

    @SerializedName("message")
    @Expose
    val message: String
)
