package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.colyak.model.data.RegisterData
import com.example.colyak.service.RegisterService


var verificationId = ""

class RegisterViewModel: ViewModel() {

    suspend fun register(registerData: RegisterData): Boolean {
        var isRegisterSuccessful = false
        try {
            val result = RegisterService.register(registerData)
            if (result != null) {
                verificationId = result
                Log.e("com.example.colyakapp.viewmodel.getVerificationId", verificationId)
                isRegisterSuccessful = true
            }
        } catch (e: Exception) {
            Log.e("registerVM", "Fail", e)
        }

        return isRegisterSuccessful
    }
}
