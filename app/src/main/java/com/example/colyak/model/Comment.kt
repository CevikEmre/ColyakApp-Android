package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Comment(
    @SerializedName("commentId")
    @Expose
    val commentId: Long,

    @SerializedName("userName")
    @Expose
    val userName: String,

    @SerializedName("receiptName")
    @Expose
    val receiptName: String,

    @SerializedName("comment")
    @Expose
    val comment: String,

    @SerializedName("createdDate")
    @Expose
    val createdDate: Date,
)
