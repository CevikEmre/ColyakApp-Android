package com.example.colyak.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.colyak.model.LoginResponse
import com.example.colyak.service.LoginService
import com.example.colyak.session.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow


var loginResponse: LoginResponse = LoginResponse("", "", "")

class LoginViewModel : ViewModel() {
    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading
    private val _checkToken = MutableStateFlow<Boolean>(false)
    val checkToken = _checkToken
    @SuppressLint("SuspiciousIndentation")
    suspend fun login(email: String, password: String,navController: NavController,context: Context): Boolean {
        var isLoginSuccessful = false
        try {
            val result = LoginService.login(context,navController,email, password)
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

    suspend fun checkToken(context: Context): Boolean {
        val sessionManager = SessionManager(context)
        try {
            _loading.value = true
            if (sessionManager.getToken() != null && sessionManager.getToken() != "") {
                _checkToken.value = true
            } else {
                _checkToken.value = false
            }
        } catch (e: Exception) {
            Log.e("LoginVM", "Fail", e)
        } finally {
            delay(2000)
            _loading.value = false
        }
        return _checkToken.value
    }
    fun clearLoginResponse(){
        loginResponse = LoginResponse("", "", "")
    }

}