package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppUser(
    @SerializedName("id")
    @Expose
    val id: Long?,

    @SerializedName("email")
    @Expose
    val email:String,

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("password")
    @Expose
    val password:String,

)
