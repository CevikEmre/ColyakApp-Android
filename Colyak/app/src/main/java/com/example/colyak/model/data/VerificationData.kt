package com.example.colyak.model.data
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class VerificationData(
    @SerializedName("verificationId")
    @Expose
    val verificationId: String,

    @SerializedName("oneTimeCode")
    @Expose
    val oneTimeCode: String
)

