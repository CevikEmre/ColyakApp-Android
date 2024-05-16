package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChoicesList(
    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("choice")
    @Expose
    val choice: String,

    @SerializedName("imageId")
    @Expose
    val imageId: Long,
)
