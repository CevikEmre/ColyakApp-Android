package com.example.colyak.service

import android.util.Log
import com.example.colyak.`interface`.RefreshTokenInterface
import com.example.colyak.model.LoginResponse
import com.example.colyak.model.TokenData
import com.example.colyak.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefreshTokenService {
    companion object {
        suspend fun refreshToken(tokenData: TokenData): LoginResponse? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(RefreshTokenInterface::class.java)
                        .refleshToken(tokenData)
                    if (response.isSuccessful) {
                        val tokenResponse = response.body()
                        Log.e("refreshToken", "Refresh token request successful: $tokenResponse")
                        tokenResponse
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "refreshToken",
                            "Refresh token request failed with code: $errorCode, message: $errorMessage"
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