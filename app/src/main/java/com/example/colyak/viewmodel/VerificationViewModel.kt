package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.colyak.model.data.VerificationData
import com.example.colyak.service.VerificationService

class VerificationViewModel : ViewModel() {
    suspend fun verification(verificationData: VerificationData): Boolean {
        var isVerifySuccesful = true
        try {
            val result = VerificationService.verification(verificationData)
            if (result != null) {
                if (result){
                    isVerifySuccesful = result
                }
                else isVerifySuccesful = false
            }

        } catch (e: Exception) {
            Log.e("verification", "Fail", e)
        }
        return isVerifySuccesful
    }
}