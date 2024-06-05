package com.example.colyak.service

import android.util.Log
import com.example.colyak.`interface`.ReceiptInterface
import com.example.colyak.model.Receipt
import com.example.colyak.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


var baseUrl = "https://api.colyakdiyabet.com.tr"

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
                        Log.e(
                            "ReceiptService",
                            "getAll request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("ReceiptService", "Error sending getAll request", e)
                    null
                }
            }
        }

        suspend fun getFavorite5Receipts(): List<Receipt?>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(ReceiptInterface::class.java)
                        .getFavorite5Receipt()
                        .awaitResponse()
                    if (response.isSuccessful) {
                        response.body()
                    } else {

                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "ReceiptService",
                            "getFavorite5Receipts request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("ReceiptService", "Error sending getFavorite5Receipts request", e)
                    null
                }
            }
        }

        suspend fun getReceiptById(receiptId: Long): Receipt? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(ReceiptInterface::class.java)
                        .getReceiptById(receiptId)
                        .awaitResponse()
                    if (response.isSuccessful) {
                        response.body()
                    } else {

                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "ReceiptService",
                            "getAll request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("ReceiptService", "Error sending getAll request", e)
                    null
                }
            }

        }
    }
}