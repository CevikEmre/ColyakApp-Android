package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("userId")
    @Expose
    val userId: String,

    @SerializedName("bolus")
    @Expose
    val bolus: Bolus,

    @SerializedName("foodList")
    @Expose
    val foodList: List<FoodList>,
)
