package com.example.colyakapp.service.interfaces


import com.example.colyak.model.Receipt
import retrofit2.Call
import retrofit2.http.GET

interface ReceiptInterface {
    @GET("/api/receipts/getAll/all")
    fun getAll(): Call<List<Receipt>>

}