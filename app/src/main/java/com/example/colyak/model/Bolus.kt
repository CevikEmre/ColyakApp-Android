package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Bolus(
    @SerializedName("bloodSugar")
    @Expose
    val bloodSugar: Long,

    @SerializedName("targetBloodSugar")
    @Expose
    val targetBloodSugar: Long,

    @SerializedName("insulinTolerateFactor")
    @Expose
    val insulinTolerateFactor: Long,

    @SerializedName("totalCarbonhydrate")
    @Expose
    val totalCarbonhydrate: Long,

    @SerializedName("insulinCarbonhydrateRatio")
    @Expose
    val insulinCarbonhydrateRatio: Long,

    @SerializedName("bolusValue")
    @Expose
    val bolusValue: Long,

    @SerializedName("eatingTime")
    @Expose
    val eatingTime: String?
)
