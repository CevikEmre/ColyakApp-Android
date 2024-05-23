package com.example.colyak.`interface`


import com.example.colyak.model.ReadyFoods
import retrofit2.Call
import retrofit2.http.GET

interface ReadyFoodInterface {
    @GET("/api/barcodes/all")
    fun getAllReadyFoods(): Call<List<ReadyFoods?>?>
}