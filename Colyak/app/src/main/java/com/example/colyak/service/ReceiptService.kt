package com.example.colyak.service

import android.util.Log
import com.example.colyak.model.Receipt
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyakapp.service.interfaces.ReceiptInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


var baseUrl  = "https://api.colyakdiyabet.com.tr"

class ReceiptService {
    companion object {
        suspend fun getAll(): List<Receipt?>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(ReceiptInterface::class.java)
                        .getAll()
                        .awaitResponse()
                    if (response.isSuccessful) {
                        response.body()
                    } else {

                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e("APIService", "getAll request failed with code: $errorCode, message: $errorMessage")
                        null
                    }

                } catch (e: Exception) {
                    Log.e("APIService", "Error sending getAll request", e)
                    null
                }
            }
        }




    }
}