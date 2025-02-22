package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PDFResponse(
    @SerializedName("id")
    @Expose
    val id:Long,

    @SerializedName("name")
    @Expose
    val name:String,
)
