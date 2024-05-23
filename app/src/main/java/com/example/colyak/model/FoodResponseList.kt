package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FoodResponseList(
    @SerializedName("foodType")
    @Expose
    val foodType: String,

    @SerializedName("carbonhydrate")
    @Expose
    val carbonhydrate: Long,

    @SerializedName("foodName")
    @Expose
    val foodName: String,
)
