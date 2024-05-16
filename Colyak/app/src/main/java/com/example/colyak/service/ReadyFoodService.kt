package com.example.colyak.service

import android.util.Log
import com.example.colyak.model.ReadyFoods
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyak.service.interfaces.ReadyFoodInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


class ReadyFoodService {
    companion object{
    suspend fun getAllReadyFoods(): List<ReadyFoods?>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.getClient(baseUrl)
                    .create(ReadyFoodInterface::class.java)
                    .getAllReadyFoods()
                    .awaitResponse()

                if (response.isSuccessful) {
                    response.body()
                }
                else {
                    val errorCode = response.code()
                    val errorMessage = response.errorBody()?.string()
                    Log.e(
                        "ReadyFoods",
                        "getAll request failed with code: $errorCode, message: $errorMessage"
                    )
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
