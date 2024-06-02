package com.example.colyak.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.colyak.service.ForgotPasswordService

class ForgotPasswordViewModel : ViewModel() {
    private val forgotPasswordResult = MutableLiveData<String?>()

    suspend fun forgotPassword(email: String, context: Context,navController: NavController): String? {
        Log.e("ForgotPasswordViewModel", "forgotPassword called")
        try {
            val response = ForgotPasswordService().forgotPassword(email, context,navController)
            if (response != null) {
                forgotPasswordResult.value = response.toString()
                forgotPasswordResult.value?.let { Log.d("ForgotPasswordViewModel", it) }
            }
            forgotPasswordResult.value?.let { Log.d("ForgotPasswordViewModel", it) }
        } catch (e: Exception) {
            Log.e("ForgotPasswordViewModel", "Fail", e)
        }
        return forgotPasswordResult.value
    }
}
