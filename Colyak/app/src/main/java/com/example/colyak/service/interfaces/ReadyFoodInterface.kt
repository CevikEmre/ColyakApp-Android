package com.example.colyak.service.interfaces


import com.example.colyak.model.ReadyFoods
import retrofit2.Call
import retrofit2.http.GET

interface ReadyFoodInterface {
    @GET("/api/ready-foods/getall")
    fun getAllReadyFoods(): Call<List<ReadyFoods?>?>
}