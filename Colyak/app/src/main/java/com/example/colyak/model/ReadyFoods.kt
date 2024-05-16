package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReadyFoods(
    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("readyFoodName")
    @Expose
    val readyFoodName: String?,


    @SerializedName("nutritionalValuesList")
    @Expose
    val nutritionalValuesList: List<NutritionalValuesList?>?,
)
