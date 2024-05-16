package com.example.colyak.service

import android.util.Log
import com.example.colyak.model.data.BolusData
import com.example.colyak.retrofit.RetrofitClient
import com.example.colyak.service.interfaces.BolusInterface

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class BolusService {
    companion object {
        suspend fun bolusFunction(bolusData: BolusData): Response<BolusData>? {
            return withContext(Dispatchers.IO) {
                try {

                    return@withContext RetrofitClient.getClient(baseUrl)
                        .create<BolusInterface?>(BolusInterface::class.java)
                        .bolusFunction(bolusData)
                } catch (e: Exception) {
                    Log.e("BOLUS", "Error sending bolus request", e)
                    null
                }
            }
        }
    }
}
