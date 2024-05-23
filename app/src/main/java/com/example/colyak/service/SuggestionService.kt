package com.example.colyak.service

import android.util.Log
import com.example.colyak.model.data.SuggestionData
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyak.`interface`.SuggestionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SuggestionService {
    companion object {
        suspend fun addSuggestion(suggestionData: SuggestionData) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(SuggestionInterface::class.java)
                        .addSuggestion(suggestionData)

                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "verification",
                            "verification request failed with code: $errorCode, message: $errorMessage"
                        )
                    }

                } catch (e: Exception) {
                    Log.e("verification", "Error sending verification request", e)
                }
            }
        }
    }
}