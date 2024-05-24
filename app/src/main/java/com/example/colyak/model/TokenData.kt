package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenData(
    @SerializedName("refreshToken")
    @Expose
    val refreshToken:String?
)

