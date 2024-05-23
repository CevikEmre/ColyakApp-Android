package com.example.colyak.service

import android.util.Log
import com.example.colyak.model.data.VerificationData
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyak.`interface`.VerificationInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VerificationService {
    companion object {
        suspend fun verification(verificationData: VerificationData): Boolean? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(VerificationInterface::class.java)
                        .verification(verificationData)

                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "verification",
                            "verification request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("verification", "Error sending verification request", e)
                    null
                }
            }
        }
    }
}