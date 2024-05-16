package com.example.colyak.service

import android.util.Log
import com.example.colyak.model.LoginResponse
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyak.service.interfaces.RefreshTokenInterface

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefreshTokenService {
    companion object {
        suspend fun refreshToken(token:String): LoginResponse? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(RefreshTokenInterface::class.java)
                        .refleshToken(token)

                    if (response.isSuccessful) {
                        response.body()
                    }
                    else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "refreshToken",
                            "refreshToken request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("refreshToken", "Error sending refreshToken request", e)
                    null
                }
            }
        }
    }
}