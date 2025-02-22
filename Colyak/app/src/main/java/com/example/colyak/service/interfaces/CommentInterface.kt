package com.example.colyak.service.interfaces


import com.example.colyak.model.Comment
import com.example.colyak.model.data.CommentData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentInterface {
    @GET("/api/comments/receipt/{receiptId}")
    fun getCommentsById(@Path("receiptId") receiptId:Long): Call<List<Comment>?>

    @POST("/api/comments/add")
    suspend fun createComment(@Body commentData: CommentData): Response<CommentData?>
    @DELETE("/api/comments/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Long):Response<Void>
}