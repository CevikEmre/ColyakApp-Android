package com.example.colyak.service

import android.util.Log
import com.example.colyak.model.data.RegisterData
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyak.`interface`.RegisterInterface

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterService {
    companion object {
        suspend fun register(registerData: RegisterData): String? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(RegisterInterface::class.java)
                        .register(registerData)

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
}