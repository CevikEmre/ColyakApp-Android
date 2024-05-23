package com.example.colyak.model

data class CommentRepliesResponse(
    val commentResponse: Comment,
    val replyResponses: List<ReplyResponse>,
)
