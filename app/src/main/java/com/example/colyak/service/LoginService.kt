package com.example.colyak.service

import android.util.Log
import com.example.colyak.`interface`.LoginInterface
import com.example.colyak.model.LoginResponse
import com.example.colyak.model.data.LoginData
import com.example.colyak.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginService {
    companion object {
        suspend fun login(email:String, password:String): LoginResponse? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(LoginInterface::class.java)
                        .login(LoginData(email,password))

                    if (response.isSuccessful) {
                        response.body()
                    }
                    else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "login",
                            "login request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("LOGIN", "Error sending login request", e)
                    null
                }
            }
        }
    }
}



