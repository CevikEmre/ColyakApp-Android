package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colyak.model.BolusReport
import com.example.colyak.service.BolusReportService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

var mealReportList = mutableListOf<BolusReport>()

class BolusReportViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _mealList = MutableStateFlow<List<BolusReport?>?>(emptyList())
    var mealList: StateFlow<List<BolusReport?>?> = _mealList

    suspend fun getBolusReports(email: String, startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = BolusReportService.getBolusReports(email, startDate, endDate)
                _mealList.value = result ?: emptyList()
                mealReportList = result as MutableList<BolusReport>
                Log.e("mealListDeneme", mealList.value.toString())

            } catch (e: Exception) {
                Log.e("ReceiptScreenVM", "Fail", e)
            } finally {
                _loading.value = false
            }
        }
    }
}