package com.example.colyak.`interface`

import com.example.colyak.model.LoginResponse
import com.example.colyak.model.TokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenInterface {
    @POST("/api/users/verify/refresh-token")
    suspend fun refleshToken(@Body tokenData: TokenData): Response<LoginResponse?>

}