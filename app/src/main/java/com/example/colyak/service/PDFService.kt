package com.example.colyak.service

import android.util.Log
import com.example.colyak.`interface`.PDFInterface
import com.example.colyak.model.PDFResponse
import com.example.colyak.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class PDFService {
    companion object {
        suspend fun getPDFs(): List<PDFResponse?>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(PDFInterface::class.java)
                        .getPDF()
                        .awaitResponse()
                    if (response.isSuccessful) {
                        response.body()
                    } else {

                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "APIService",
                            "getAll request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("APIService", "Error sending getAll request", e)
                    null
                }
            }
        }
    }
}
