package com.example.colyak.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForgotPasswordData(
    @SerializedName("email")
    @Expose
    val email: String
)
