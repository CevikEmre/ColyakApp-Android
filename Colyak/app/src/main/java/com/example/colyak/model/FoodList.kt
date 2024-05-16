package com.example.colyak.model

import com.example.colyak.model.enum.FoodType
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FoodList(
    @SerializedName("foodType")
    @Expose
    val foodType: FoodType,

    @SerializedName("foodId")
    @Expose
    val foodId: Long,

    @SerializedName("carbonhydrate")
    @Expose
    val carbonhydrate: Long,
)
