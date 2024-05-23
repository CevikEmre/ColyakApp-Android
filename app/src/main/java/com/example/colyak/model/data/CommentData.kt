package com.example.colyak.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CommentData(
    @SerializedName("receiptId")
    @Expose
    val receiptId: Long,

    @SerializedName("comment")
    @Expose
    val comment: String,
)
