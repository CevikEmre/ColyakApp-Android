package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NutritionalValuesList(
    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("unit")
    @Expose
    val unit: Double?,

    @SerializedName("type")
    @Expose
    val type: String?,

    @SerializedName("carbohydrateAmount")
    @Expose
    val carbohydrateAmount: Double?,

    @SerializedName("proteinAmount")
    @Expose
    val proteinAmount: Double?,

    @SerializedName("fatAmount")
    @Expose
    val fatAmount: Double?,

    @SerializedName("calorieAmount")
    @Expose
    val calorieAmount: Double?,
)

