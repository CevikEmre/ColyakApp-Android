package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.Date

data class ReplyResponse(
    @SerializedName("replyId")
    @Expose
    val replyId: Long,

    @SerializedName("userName")
    @Expose
    val userName: String,

    @SerializedName("createdDate")
    @Expose
    val createdDate: Date,

    @SerializedName("reply")
    @Expose
    val reply: String,
)
