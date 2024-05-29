package com.example.colyak.`interface`


import com.example.colyak.model.Receipt
import retrofit2.Call
import retrofit2.http.GET

interface ReceiptInterface {
    @GET("/api/receipts/getAll/all")
    fun getAll(): Call<List<Receipt>>
    @GET("/api/meals/report/top5receipts")
    fun getFavorite5Receipt(): Call<List<Receipt>>
}