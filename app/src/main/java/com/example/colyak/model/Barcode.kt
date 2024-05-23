package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Barcode(
    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("code")
    @Expose
    val code: Long?,

    @SerializedName("name")
    @Expose
    val name: String?,

    @SerializedName("imageId")
    @Expose
    val imageId: Long?,

    @SerializedName("glutenFree")
    @Expose
    val glutenFree: Boolean?,

    @SerializedName("deleted")
    @Expose
    val deleted: Boolean?,

    @SerializedName("nutritionalValuesList")
    @Expose
    val nutritionalValuesList: List<NutritionalValuesList?>?,
)
