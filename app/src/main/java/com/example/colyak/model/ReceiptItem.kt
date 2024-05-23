package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReceiptItem(
    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("productName")
    @Expose
    val productName: String,

    @SerializedName("unit")
    @Expose
    val unit: Double,

    @SerializedName("type")
    @Expose
    val type: String,
)
