package com.example.colyak.`interface`

import com.example.colyak.model.LoginResponse
import com.example.colyak.model.data.LoginData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginInterface {
   @POST("/api/users/verify/login")
   suspend fun login(@Body loginData: LoginData): Response<LoginResponse?>
}