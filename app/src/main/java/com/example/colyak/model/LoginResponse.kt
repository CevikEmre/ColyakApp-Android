package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    @Expose
    var token: String,

    @SerializedName("refreshToken")
    @Expose
    var refreshToken: String,

    @SerializedName("userName")
    @Expose
    var userName: String,

)
