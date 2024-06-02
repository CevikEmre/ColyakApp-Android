package com.example.colyak.service

import android.util.Log
import barcodeAlert
import com.example.colyak.`interface`.BarcodeInterface
import com.example.colyak.model.Barcode
import com.example.colyak.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class BarcodeService {
    companion object {
        suspend fun getBarcode(code:String): Barcode? {
            return withContext(Dispatchers.IO) {
                try {
                    val response = RetrofitClient.getClient(baseUrl)
                        .create(BarcodeInterface::class.java)
                        .getBarcode(code)
                        .awaitResponse()
                    if (response.isSuccessful) {
                        response.body()
                    } else {

                        val errorCode = response.code()
                        Log.e("BarcodeService", "getBarcode request failed with code: $errorCode")
                        if (errorCode == 404){
                            barcodeAlert = true
                            }
                        val errorMessage = response.errorBody()?.string()
                        Log.e("BarcodeService", "getBarcode request failed with code: $errorCode, message: $errorMessage")
                        null
                    }

                } catch (e: Exception) {
                    Log.e("BarcodeService", "Error sending getBarcode request", e)
                    null
                }
            }
        }
    }
}