package com.example.colyak.service.interfaces


import com.example.colyak.model.data.RegisterData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterInterface {
    @POST("/api/users/verify/create")
    suspend fun register(@Body registerData: RegisterData): Response<String?>
}