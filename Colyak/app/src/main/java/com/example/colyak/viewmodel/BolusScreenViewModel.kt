package com.example.colyak.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.colyak.model.Bolus
import com.example.colyak.model.data.BolusData
import com.example.colyak.service.BolusService

import kotlinx.coroutines.flow.MutableStateFlow

class BolusScreenViewModel:ViewModel() {
    private val _bolus = MutableStateFlow(BolusData( emptyList(), Bolus(0,0,0,0,0,0)))

    @SuppressLint("SuspiciousIndentation")
    suspend fun bolus(bolusData: BolusData) {
        try {
            val result = BolusService.bolusFunction(bolusData)
            if (result != null) {
                _bolus.value = result.body()!!
            }
            Log.e("BolusVM", _bolus.value.toString())


        } catch (e: Exception) {
            Log.e("BolusVM", "Fail", e)
        }
    }
}