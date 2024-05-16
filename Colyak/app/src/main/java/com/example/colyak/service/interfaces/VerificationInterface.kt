package com.example.colyak.service.interfaces

import com.example.colyak.model.data.VerificationData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VerificationInterface {
    @POST("/api/users/verify/verify-email")
    suspend fun verification(@Body verificationData: VerificationData):Response<Boolean?>
}