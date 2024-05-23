package com.example.colyak.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Reply(
    @SerializedName("replyId")
    @Expose
    val replyId: Long,

    @SerializedName("userName")
    @Expose
    val userName: String,


    @SerializedName("createdDate")
    @Expose
    val createdDate: String,

    @SerializedName("reply")
    @Expose
    val reply: String,
)
