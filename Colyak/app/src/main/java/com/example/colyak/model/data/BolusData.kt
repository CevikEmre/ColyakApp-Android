package com.example.colyak.model.data

import com.example.colyak.model.Bolus
import com.example.colyak.model.FoodList
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BolusData(

    @SerializedName("foodList")
    @Expose
    val foodList: List<FoodList>,

    @SerializedName("bolus")
    @Expose
    val bolus: Bolus
)
