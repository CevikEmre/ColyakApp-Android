package com.example.colyak.`interface`

import com.example.colyak.model.BolusReport
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDate

interface BolusReportInterface {
    @GET("/api/meals/report/{email}/{startDate}/{endDate}")
    fun getMealReport(@Path("email") email: String,@Path("startDate") startDate: LocalDate ,@Path("endDate") endDate: LocalDate) :Call<List<BolusReport?>?>
}