package com.example.colyak.service.interfaces

import com.example.colyak.model.data.BolusData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BolusInterface {
    @POST("/api/meals/add")
    suspend fun bolusFunction(@Body bolusData: BolusData): Response<BolusData>
}