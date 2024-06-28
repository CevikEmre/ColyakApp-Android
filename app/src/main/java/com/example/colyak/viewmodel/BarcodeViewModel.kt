package com.example.colyak.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colyak.model.Barcode
import com.example.colyak.service.BarcodeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

var barcode = Barcode(0, 0, "", 0, false, false, emptyList())
class BarcodeViewModel : ViewModel() {
    private val _barcode = MutableLiveData(Barcode(0, 0, "", 0, false, false, emptyList()))
    //val barcode: MutableLiveData<Barcode> = _barcode
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    suspend fun getBarcode(code:String):Barcode? {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = BarcodeService.getBarcode(code)
                if (result != null) {
                    _barcode.value = result
                    barcode = result
                }
            } catch (e: Exception) {
                Log.e("BarcodeViewModel", "Fail", e)
            } finally {
                _loading.value = false
            }
        }
        return _barcode.value
    }
}