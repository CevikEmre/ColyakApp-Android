package com.example.colyak.service.interfaces


import com.example.colyak.model.CommentRepliesResponse
import com.example.colyak.model.Reply
import com.example.colyak.model.ReplyResponse
import com.example.colyak.model.data.ReplyData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReplyInterface {
    @GET("/api/replies/comments/{commentId}")
    fun getCommentRepliesById(@Path("commentId") commentId: Long): Call<List<ReplyResponse?>?>

    @POST("/api/replies/add")
    suspend fun createReply(@Body replyData: ReplyData): Response<Reply?>

    @GET("/api/replies/receipt/commentsWithReplyByReceiptId/{receiptId}}")
    fun getCommentRepliesByReceiptId(@Path("receiptId") receiptId: Long): Call<List<CommentRepliesResponse?>?>
    @DELETE("/api/replies/{replyId}")
    suspend fun deleteReply(@Path("replyId") replyId: Long): Response<Void>
}