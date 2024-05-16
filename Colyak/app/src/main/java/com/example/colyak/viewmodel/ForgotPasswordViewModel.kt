package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.colyak.service.ForgotPasswordService


class ForgotPasswordViewModel : ViewModel() {
    suspend fun forgotPassword(email: String) {
        try {
            ForgotPasswordService().forgotPassword(email)

        } catch (e: Exception) {
            Log.e("ForgotPasswordViewModel", "Fail", e)
        }
    }
}