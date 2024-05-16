package com.example.colyakapp.service.interfaces


import com.example.colyak.model.Receipt
import com.example.colyak.model.data.FavoriteData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FavoriteInterface {
    @GET("/api/likes/favoriteList")
   fun getAllFavoriteReceipts(): Call<List<Receipt>?>

    @POST("/api/likes/like")
    suspend fun likeReceipt(@Body favoriteData: FavoriteData): Response<String>

    @POST("/api/likes/unlike")
    suspend fun unlikeReceipt(@Body favoriteData: FavoriteData): Response<String>
}
