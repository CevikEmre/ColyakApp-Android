package com.example.colyak.service

import android.util.Log
import com.example.colyak.model.Receipt
import com.example.colyak.model.data.FavoriteData
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyakapp.service.interfaces.FavoriteInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class FavoriteService {
    companion object {
        suspend fun getAllFavoriteReceipts(): List<Receipt>? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(FavoriteInterface::class.java)
                        .getAllFavoriteReceipts()
                        .awaitResponse()
                    if (response.isSuccessful) {
                        response.body() ?: emptyList()
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "FavoriteService",
                            "getAllFavoriteReceipts request failed with code: $errorCode, message: $errorMessage"
                        )
                        null
                    }

                } catch (e: Exception) {
                    Log.e("FavoriteService", "Ana iş iptal ediliyor")
                    Log.e("FavoriteService", "Error sending getAllFavoriteReceipts request", e)
                    null
                }
            }
        }


        suspend fun likeReceipt(favoriteData: FavoriteData){
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(FavoriteInterface::class.java)
                        .likeReceipt(favoriteData)
                    if (response.isSuccessful) {
                        response.body()

                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "FavoriteService",
                            "likeReceipt request failed with code: $errorCode, message: $errorMessage"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("FavoriteService", "Error sending likeReceipt request", e)
                }
            }
        }

        suspend fun unlikeReceipt(favoriteData: FavoriteData) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(FavoriteInterface::class.java)
                        .unlikeReceipt(favoriteData)
                    if (response.isSuccessful) {
                        response.body()
                    } else {
                        val errorCode = response.code()
                        val errorMessage = response.errorBody()?.string()
                        Log.e(
                            "FavoriteService",
                            "unlikeReceipt request failed with code: $errorCode, message: $errorMessage"
                        )
                    }

                } catch (e: Exception) {
                    Log.e("FavoriteService", "Error sending unlikeReceipt request", e)

                }
            }
        }


    }
}