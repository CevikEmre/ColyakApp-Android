package com.example.colyak.service

import android.util.Log
import com.example.colyak.`interface`.BolusReportInterface
import com.example.colyak.model.BolusReport
import com.example.colyak.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.time.LocalDate

class BolusReportService {
    companion object {
        suspend fun getBolusReports(email: String, startDate: LocalDate, endDate: LocalDate): List<BolusReport?>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(BolusReportInterface::class.java)
                        .getMealReport(email,startDate,endDate)
                        .awaitResponse()
                    if (response.isSuccessful) {
                        response.body()
                    } else {

                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "BolusReportService",
                            "getBolusReports request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("BolusReportService", "Error sending getBolusReports request", e)
                    null
                }
            }
        }
    }
}