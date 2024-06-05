package com.example.colyak.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Receipt(
    @SerializedName("receiptDetails")
    @Expose
    val receiptDetails: List<String?>?,

    @SerializedName("receiptItems")
    @Expose
    val receiptItems: List<ReceiptItem?>?,

    @SerializedName("receiptName")
    @Expose
    val receiptName: String?,

    @SerializedName("nutritionalValuesList")
    @Expose
    val nutritionalValuesList: List<NutritionalValuesList?>?,

    @SerializedName("imageId")
    @Expose
    val imageId: Long?,

    @SerializedName("id")
    @Expose
    val id: Long?
)



