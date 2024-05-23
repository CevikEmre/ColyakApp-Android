package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BolusResponse(
    @SerializedName("userName")
    @Expose
    val userName: String,

    @SerializedName("foodResponseList")
    @Expose
    val foodResponseList: List<FoodResponseList>,

    @SerializedName("bolus")
    @Expose
    val bolus: Bolus,

    @SerializedName("dateTime")
    @Expose
    val dateTime: String,
)
