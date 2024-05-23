package com.example.colyak.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.colyak.model.data.BolusData
import com.example.colyak.service.BolusService

class BolusScreenViewModel : ViewModel() {
    @SuppressLint("NewApi")

    suspend fun bolus(bolusData: BolusData) {
        try {
            val response = BolusService.bolusFunction(bolusData)
            Log.e("BolusResponse", response.toString())
        } catch (e: Exception) {
            Log.e("BolusVM", "Fail", e)
        }
    }
}