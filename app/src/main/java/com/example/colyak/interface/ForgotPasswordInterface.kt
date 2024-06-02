package com.example.colyak.`interface`

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface ForgotPasswordInterface {

    @POST("/api/users/verify/x0/{email}")
    suspend fun forgotPassword(@Path("email") email: String): Response<Unit?>
}