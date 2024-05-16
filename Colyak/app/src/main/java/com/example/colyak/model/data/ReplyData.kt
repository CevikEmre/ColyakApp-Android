package com.example.colyak.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReplyData(
    @SerializedName("commentId")
    @Expose
    val commentId: Long,

    @SerializedName("reply")
    @Expose
    val reply: String,

)
