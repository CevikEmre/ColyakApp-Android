package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.colyak.model.data.SuggestionData
import com.example.colyak.service.SuggestionService

class SuggestionViewModel:ViewModel() {
    suspend fun addSuggestion(suggestionData: SuggestionData) {
        try {
            SuggestionService.addSuggestion(suggestionData)

        } catch (e: Exception) {
            Log.e("ForgotPasswordViewModel", "Fail", e)
        }
    }
}