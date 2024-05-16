package com.example.colyak.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterData(
    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("password")
    @Expose
    val password: String
)
