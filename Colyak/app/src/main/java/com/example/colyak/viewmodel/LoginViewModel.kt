package com.example.colyak.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.colyak.model.LoginResponse
import com.example.colyak.service.LoginService


var loginResponse: LoginResponse = LoginResponse("","","")
class LoginViewModel : ViewModel() {
    @SuppressLint("SuspiciousIndentation")
    suspend fun login(email: String, password: String): Boolean {
        var isLoginSuccessful = false
            try {
                val result = LoginService.login(email, password)
                if (result != null) {
                    loginResponse = result
                    isLoginSuccessful = true
                }
                Log.e("XYZ", loginResponse.toString())

            } catch (e: Exception) {
                Log.e("LoginVM", "Fail", e)
            }

        return isLoginSuccessful
    }
}