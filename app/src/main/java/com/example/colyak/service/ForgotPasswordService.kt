package com.example.colyak.service


import android.util.Log
import com.example.colyak.`interface`.ForgotPasswordInterface
import com.example.colyak.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForgotPasswordService {

    suspend fun forgotPassword(email: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(ForgotPasswordInterface::class.java)
                    .forgotPassword(email)

                if (response.isSuccessful) {
                    response.body()
                }
                else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e(
                        "register",
                        "register request failed with code: $errorCode, message: $errorMessage"
                    )
                    null
                }

            } catch (e: Exception) {
                Log.e("register", "Error sending register request", e)
                null
            }
        }
    }
}